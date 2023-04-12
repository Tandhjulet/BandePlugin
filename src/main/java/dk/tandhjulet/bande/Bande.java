package dk.tandhjulet.bande;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.AbstractMap.SimpleEntry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.enums.BandeRank;
import dk.tandhjulet.events.BandeBankUpdateEvent;
import dk.tandhjulet.events.BandeNewAccessEvent;
import dk.tandhjulet.hooks.WorldGuardHook;
import dk.tandhjulet.storage.Config;
import dk.tandhjulet.storage.Message;

public class Bande implements Serializable {

    private transient Timer timer;

    private static transient final long serialVersionUID = 1L;
    private transient boolean dirty;

    private String name;
    private HashMap<BandeRank, HashSet<UUID>> members;
    private HashSet<String> alliances;
    private HashSet<String> rivals;
    private Integer level;

    private String bandeHus;
    private List<String> access;

    private HashSet<UUID> invitations;
    private HashSet<String> incommingAllyInvitations;

    private Integer maxMembers;
    private Integer maxAlliances;
    private Integer maxRivals;

    private Integer allySkade;
    private Integer bandeSkade;

    private BigDecimal balance;

    public Bande(String name, UUID owner) {
        this.name = name;
        members = new HashMap<BandeRank, HashSet<UUID>>() {
            {
                put(BandeRank.EJER, new HashSet<>());
                put(BandeRank.ADMIN, new HashSet<>());
                put(BandeRank.MOD, new HashSet<>());
                put(BandeRank.MEDLEM, new HashSet<>());
            }
        };

        alliances = new HashSet<String>();
        rivals = new HashSet<String>();

        balance = new BigDecimal(0);
        level = 1;

        Config conf = BandePlugin.getConfiguration();
        maxMembers = conf.getStartMembers();
        maxAlliances = conf.getStartAlly();
        maxRivals = conf.getStartRival();

        allySkade = 100;
        bandeSkade = 100;

        incommingAllyInvitations = new HashSet<>();
        invitations = new HashSet<>();

        access = new LinkedList<>();

        addMember(BandeRank.EJER, owner);

        BandePlugin.getTop().setLevel(level, getName());
        forceSave();
        setDirty(false);
    }

    public Timer getTimer() {
        return timer;
    }

    public List<String> getAccessList() {
        return access;
    }

    public void setBandeHus(String hus) {
        bandeHus = hus;
        setDirty(true);
    }

    public String getBandeHus() {
        return bandeHus;
    }

    public Boolean hasBandeHus() {
        return bandeHus != null && bandeHus != "";
    }

    public void addAccess(String regionName) {
        access.add(regionName);
        setDirty(true);

        Bukkit.getPluginManager().callEvent(new BandeNewAccessEvent(this, regionName));
    }

    public HashSet<String> getAllianceInvites() {
        return incommingAllyInvitations;
    }

    public void addAllyInvite(Bande from, BandePlayer sender) {
        if (from == this) {
            Message.sendReplaced(sender, "ally.invite_own_bande", null);
            return;
        }
        if (incommingAllyInvitations.contains(from.getName())) {
            Message.sendReplaced(sender, "ally.already_sent_invite", null, this.getName());
            return;
        }
        incommingAllyInvitations.add(from.getName());

        Message.sendReplaced(getMemberIterable(), "ally.invite_message_from",
                new SimpleEntry<String, String>("run_command", "/bande ally " + from.getName()), from.getName());
        Message.sendReplaced(from.getMemberIterable(), "ally.invite_message_to", null, this.getName());

        setDirty(true);
    }

    public void init() {
        this.timer = new Timer();
        timer.schedule(new BackgroundSaver(), 1000 * 20, 1000 * 20);
    }

    public Iterable<UUID> getMemberIterable() {
        return Iterables.concat(getMembers().values());
    }

    public void removeInvite(UUID uuid) {
        invitations.remove(uuid);
        setDirty(true);
    }

    public boolean kick(BandePlayer sender, UUID uuid) {
        List<UUID> members = ImmutableList.copyOf(getMemberIterable());

        if (!members.contains(uuid)) {
            Message.sendReplaced(sender, "kick.player_not_in_bande", null);
            return false;
        }

        BandePlayer player = BandePlugin.getAPI().getPlayer(uuid);
        if (player.getBandeRank().isHigherThanOrEqualTo(sender.getBandeRank())) {
            Message.sendReplaced(sender, "bande_command.not_enough_authority", null, player.getBase().getName());
            return false;
        }

        Message.sendReplaced(members, "kick.message_members", null, player.getBase().getName(),
                sender.getBase().getName());

        this.removeMember(player.getBandeRank(), uuid);
        player.setBande(null, null);

        player.forceSave();
        this.forceSave();

        return true;
    }

    public boolean invite(Player sender, UUID player) {
        Player p = Bukkit.getPlayer(player);
        if (p == null) {
            Message.sendReplaced(sender, "invite.not_online", null);
            return false;
        }

        if (getMaxMembers() == getMemberIterable().spliterator().getExactSizeIfKnown()) {
            Message.sendReplaced(sender, "invite.max_members", null);
            return false;
        }

        BandePlayer bandePlayer = BandePlugin.getAPI().getPlayer(p);
        if (bandePlayer.getInvitations().contains(getName())) {
            Message.sendReplaced(sender, "invite.already_sent_invite", null, p.getName());
            return false;
        }

        invitations.add(p.getUniqueId());
        bandePlayer.addInvitation(getName());

        Message.sendReplaced(p, "invite.to_invited", null, getName());

        Iterable<UUID> members = getMemberIterable();

        Message.sendReplaced(members, "invite.to_members", null, sender.getName(), p.getName());
        return true;
    }

    public Integer getAllySkade() {
        return allySkade;
    }

    public void removeAllySkade() {
        allySkade -= 1;
        setDirty(true);
    }

    public void addMaxMember() {
        maxMembers += 1;
        setDirty(true);
    }

    public Integer getBandeSkade() {
        return bandeSkade;
    }

    public void removeBandeSkade() {
        bandeSkade -= 1;
        setDirty(true);
    }

    public Integer getMaxMembers() {
        return maxMembers;
    }

    public Integer getMaxAlliances() {
        return maxAlliances;
    }

    public Integer getMaxRivals() {
        return maxRivals;
    }

    public HashSet<String> getRivals() {
        return rivals;
    }

    public int getRivalAmount() {
        return rivals.size();
    }

    public HashSet<String> getAlliances() {
        return alliances;
    }

    public int getAllianceAmount() {
        return alliances.size();
    }

    public HashMap<BandeRank, HashSet<UUID>> getMembers() {
        return members;
    }

    public Integer getLevel() {
        return level;
    }

    public void addLevel() {
        level += 1;
        BandePlugin.getTop().setLevel(level, getName());
        setDirty(true);
    }

    public UUID getOwner() {
        return this.getMembers().get(BandeRank.EJER).iterator().next();
    }

    public String getName() {
        return name;
    }

    public void addAlliance(Bande alliance) {
        alliances.add(alliance.getName());
        setDirty(true);

        forceSave();
    }

    public void removeAllianceInvite(String name) {
        incommingAllyInvitations.remove(name);
        setDirty(true);
    }

    public boolean isMaxAlliances() {
        return maxAlliances <= alliances.size();
    }

    public boolean isMaxRivals() {
        return maxAlliances <= alliances.size();
    }

    public void addMaxAlliance() {
        maxAlliances += 1;
        setDirty(true);
    }

    public void addMaxRivals() {
        maxRivals += 1;
        setDirty(true);
    }

    public void removeAlliance(Bande alliance) {
        alliances.remove(alliance.getName());
        setDirty(true);

        forceSave();
    }

    public void addRival(Bande rival) {
        rivals.add(rival.getName());
        setDirty(true);
    }

    public void removeRival(Bande rival) {
        rivals.remove(rival.getName());
        setDirty(true);
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void addToBalance(BigDecimal amount) {
        this.balance = getBalance().add(amount);
        setDirty(true);
        Bukkit.getPluginManager().callEvent(new BandeBankUpdateEvent(this, amount, getBalance()));
    }

    public void setBalance(BigDecimal newBalance) {
        this.balance = newBalance;
        setDirty(true);
        Bukkit.getPluginManager().callEvent(new BandeBankUpdateEvent(this, newBalance, newBalance));
    }

    public void removeBalance(BigDecimal toRemove) {
        this.balance = balance.subtract(toRemove);
        setDirty(true);
        Bukkit.getPluginManager()
                .callEvent(new BandeBankUpdateEvent(this, toRemove.multiply(new BigDecimal(-1)), getBalance()));
    }

    public void addMember(BandeRank rank, UUID user) {

        for (String access : getAccessList()) {
            WorldGuardHook.addMember(access, user, BandePlugin.getConfiguration().getMainWorld());
        }

        HashSet<UUID> uuids = members.get(rank);
        uuids.add(user);
        members.put(rank, uuids);

        setDirty(true);
    }

    public void removeMember(BandeRank rank, UUID user) {

        for (String access : getAccessList()) {
            WorldGuardHook.removeMember(access, user, BandePlugin.getConfiguration().getMainWorld());
        }

        HashSet<UUID> uuids = members.get(rank);
        uuids.remove(user);
        members.put(rank, uuids);

        setDirty(true);
    }

    public void forceSave() {
        BandePlugin.getFileManager().saveBande(getName(), this);
        setDirty(false);
    }

    public void invalidate() {
        BandePlugin.getAPI().discardCache(this);
    }

    public void updateCache() {
        BandePlugin.getAPI().addToCache(getName(), this);
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
        updateCache();
    }

    public boolean isDirty() {
        return dirty;
    }

    private class BackgroundSaver extends TimerTask {
        @Override
        public synchronized void run() {
            if (isDirty()) {
                BandePlugin.getFileManager().saveBande(getName(), Bande.this);
                setDirty(false);
            }
        }
    }
}
