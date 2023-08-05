package dk.tandhjulet.bande;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.AbstractMap.SimpleEntry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.config.BandeConfig;
import dk.tandhjulet.config.IConfig;
import dk.tandhjulet.config.holder.BandeHolder;
import dk.tandhjulet.enums.BandeRank;
import dk.tandhjulet.events.BandeBankUpdateEvent;
import dk.tandhjulet.events.BandeNewAccessEvent;
import dk.tandhjulet.hooks.WorldGuardHook;
import dk.tandhjulet.migrator.Migrate;
import dk.tandhjulet.storage.Message;

public class Bande implements IConfig {
    private final BandeConfig config;
    private BandeHolder holder;

    private transient boolean isDestroyed = false;

    public Bande(String name, UUID owner) {
        final File folder = new File(BandePlugin.getPlugin().getDataFolder(), "userdata");
        if (!folder.exists() && !folder.mkdirs()) {
            throw new RuntimeException("Unable to create userdata folder!");
        }

        config = new BandeConfig(new File(folder, name + ".yml"));
        config.setSaveHook(() -> {
            config.setRootHolder(BandeHolder.class, holder);
        });

        reloadConfig();

        if (holder.name() == null) { // Assume first initialization
            holder.name(name);
            BandePlugin.getTop().setLevel(1, getName());
            addMember(BandeRank.EJER, owner);
        }

        save();
    }

    public void destroy() {
        config.getFile().deleteOnExit();
        this.isDestroyed = true;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public List<String> getAccessList() {
        return holder.access();
    }

    public void setBandeHus(String hus) {
        holder.bandeHus(hus);
        save();
    }

    public String getBandeHus() {
        return holder.bandeHus();
    }

    public Boolean hasBandeHus() {
        return holder.bandeHus() != null && holder.bandeHus() != "";
    }

    public void addAccess(String regionName) {
        List<String> access = holder.access();
        access.add(regionName);
        holder.access(access);

        save();
        Bukkit.getPluginManager().callEvent(new BandeNewAccessEvent(this, regionName));
    }

    public HashSet<String> getAllianceInvites() {
        return holder.incommingAllyInvitations();
    }

    public void addAllyInvite(Bande from, BandePlayer sender) {
        HashSet<String> incommingAllyInvitations = holder.incommingAllyInvitations();

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

        save();
    }

    public Iterable<UUID> getMemberIterable() {
        return Iterables.concat(getMembers().values());
    }

    public void removeInvite(UUID uuid) {
        HashSet<UUID> invitations = holder.invitations();
        invitations.remove(uuid);
        holder.invitations(invitations);

        save();
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

        this.save();

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

        addInvitation(p.getUniqueId());
        bandePlayer.addInvitation(getName());

        Message.sendReplaced(p, "invite.to_invited", null, getName());

        Iterable<UUID> members = getMemberIterable();

        Message.sendReplaced(members, "invite.to_members", null, sender.getName(), p.getName());
        return true;
    }

    public void addInvitation(UUID toInvite) {
        HashSet<UUID> invitations = holder.invitations();
        invitations.add(toInvite);
        holder.invitations(invitations);

        save();
    }

    public Integer getAllySkade() {
        return holder.allySkade();
    }

    public void removeAllySkade() {
        holder.allySkade((holder.allySkade() - 1));
        save();
    }

    public void addMaxMember() {
        holder.maxMembers((holder.maxMembers() + 1));
        save();
    }

    public Integer getBandeSkade() {
        return holder.bandeSkade();
    }

    public void removeBandeSkade() {
        holder.bandeSkade((holder.bandeSkade() - 1));
        save();
    }

    public Integer getMaxMembers() {
        return holder.maxMembers();
    }

    public Integer getMaxAlliances() {
        return holder.maxAlliances();
    }

    public Integer getMaxRivals() {
        return holder.maxRivals();
    }

    public HashSet<String> getRivals() {
        return holder.rivals();
    }

    public int getRivalAmount() {
        return getRivals().size();
    }

    public HashSet<String> getAlliances() {
        return holder.alliances();
    }

    public int getAllianceAmount() {
        return getAlliances().size();
    }

    public HashMap<BandeRank, HashSet<UUID>> getMembers() {
        return holder.members();
    }

    public Integer getLevel() {
        return holder.level();
    }

    public void addLevel() {
        BandePlugin.getTop().setLevel(getLevel() + 1, getName());

        holder.level(getLevel() + 1);
        save();
    }

    public UUID getOwner() {
        return this.getMembers().get(BandeRank.EJER).iterator().next();
    }

    public String getName() {
        return holder.name();
    }

    public void addAlliance(Bande alliance) {
        HashSet<String> alliances = holder.alliances();
        alliances.add(alliance.getName());
        holder.alliances(alliances);

        save();
    }

    public void removeAllianceInvite(String name) {
        HashSet<String> incommingAllyInvitations = holder.incommingAllyInvitations();
        incommingAllyInvitations.add(name);
        holder.incommingAllyInvitations(incommingAllyInvitations);

        save();
    }

    public boolean isMaxAlliances() {
        return holder.maxAlliances() <= getAllianceAmount();
    }

    public boolean isMaxRivals() {
        return holder.maxRivals() <= getRivalAmount();
    }

    public void addMaxAlliance() {
        holder.maxAlliances(holder.maxAlliances() + 1);
        save();
    }

    public void addMaxRivals() {
        holder.maxRivals(holder.maxRivals() + 1);
        save();
    }

    public void removeAlliance(Bande alliance) {
        HashSet<String> alliances = holder.alliances();
        alliances.remove(alliance.getName());
        holder.alliances(alliances);

        save();
    }

    public void addRival(Bande rival) {
        HashSet<String> rivals = holder.rivals();
        rivals.add(rival.getName());
        holder.rivals(rivals);

        save();
    }

    public void removeRival(Bande rival) {
        HashSet<String> rivals = holder.rivals();
        rivals.remove(rival.getName());
        holder.rivals(rivals);

        save();
    }

    public BigDecimal getBalance() {
        return holder.balance();
    }

    public void addToBalance(BigDecimal amount) {
        holder.balance(getBalance().add(amount));
        save();

        Bukkit.getPluginManager().callEvent(new BandeBankUpdateEvent(this, amount, getBalance()));
    }

    public void setBalance(BigDecimal newBalance) {
        holder.balance(newBalance);
        save();

        Bukkit.getPluginManager().callEvent(new BandeBankUpdateEvent(this, newBalance, newBalance));
    }

    public void removeBalance(BigDecimal toRemove) {
        holder.balance(getBalance().subtract(toRemove));
        save();

        Bukkit.getPluginManager()
                .callEvent(new BandeBankUpdateEvent(this, toRemove.multiply(new BigDecimal(-1)), getBalance()));
    }

    public void addMember(BandeRank rank, UUID user) {

        for (String access : getAccessList()) {
            WorldGuardHook.addMember(access, user, BandePlugin.getConfiguration().getMainWorld());
        }

        HashMap<BandeRank, HashSet<UUID>> members = getMembers();
        HashSet<UUID> uuids = members.get(rank);
        uuids.add(user);

        members.put(rank, uuids);
        holder.members(members);

        save();
    }

    public void removeMember(BandeRank rank, UUID user) {

        for (String access : getAccessList()) {
            WorldGuardHook.removeMember(access, user, BandePlugin.getConfiguration().getMainWorld());
        }

        HashMap<BandeRank, HashSet<UUID>> members = getMembers();
        HashSet<UUID> uuids = members.get(rank);
        uuids.remove(user);

        members.put(rank, uuids);
        holder.members(members);

        save();
    }

    private void updateCache() {
        BandePlugin.getAPI().addToCache(getName(), this);
    }

    public void invalidate() {
        BandePlugin.getAPI().discardCache(this);
    }

    private void save() {
        config.save();
        updateCache();
    }

    @Override
    public void reloadConfig() {
        config.load();
    }

    @Deprecated
    @Migrate
    private String name;

    @Deprecated
    @Migrate
    private HashMap<BandeRank, HashSet<UUID>> members;

    @Deprecated
    @Migrate
    private HashSet<String> alliances;

    @Deprecated
    @Migrate
    private HashSet<String> rivals;

    @Deprecated
    @Migrate
    private Integer level;

    @Deprecated
    @Migrate
    private String bandeHus;

    @Deprecated
    @Migrate
    private List<String> access;

    @Deprecated
    @Migrate
    private HashSet<UUID> invitations;

    @Deprecated
    @Migrate
    private HashSet<String> incommingAllyInvitations;

    @Deprecated
    @Migrate
    private Integer maxMembers;

    @Deprecated
    @Migrate
    private Integer maxAlliances;

    @Deprecated
    @Migrate
    private Integer maxRivals;

    @Deprecated
    @Migrate
    private Integer allySkade;

    @Deprecated
    @Migrate
    private Integer bandeSkade;

    @Deprecated
    @Migrate
    private BigDecimal balance;
}
