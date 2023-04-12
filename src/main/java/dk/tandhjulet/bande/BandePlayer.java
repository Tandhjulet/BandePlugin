package dk.tandhjulet.bande;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.enums.BandeRank;
import dk.tandhjulet.enums.ChatStatus;

public class BandePlayer implements Serializable {

    public transient Timer timer;
    private transient Player cachedPlayer;

    private static transient final long serialVersionUID = 2L;
    private transient boolean dirty = true;

    private transient ChatStatus disabled;
    private transient String previousGui;

    private UUID base;
    private String bande;
    private BandeRank bandeRank;
    private HashSet<String> invitations;

    public BandePlayer(Player base) {
        this.base = base.getUniqueId();
        this.invitations = new HashSet<>();

        this.bande = null;
        this.bandeRank = null;
    }

    public void init() {
        this.timer = new Timer();
        timer.schedule(new BackgroundSaver(), 1000 * 30, 1000 * 30);
    }

    public String getPreviousGUI() {
        return previousGui;
    }

    public void setBase(Player base) {
        if (this.base != base.getUniqueId()) {
            this.base = base.getUniqueId();
            setDirty(true);
        }
    }

    public void setPreviousGUI(String id) {
        previousGui = id;
        updateCache();
    }

    public Timer getTimer() {
        return timer;
    }

    public void setBandeRank(BandeRank rank) {
        this.bandeRank = rank;
        setDirty(true);
    }

    public HashSet<String> getInvitations() {
        return invitations;
    }

    public void addInvitation(String bandeName) {
        invitations.add(bandeName);
        setDirty(true);
    }

    public void removeInvitation(String bandeName) {
        invitations.remove(bandeName);
        setDirty(true);
    }

    public void setChat(ChatStatus chatReason) {
        this.disabled = chatReason;
    }

    public ChatStatus getChat() {
        if (disabled == null || disabled == ChatStatus.NULL)
            return ChatStatus.NULL;

        return disabled;
    }

    public boolean getChatStatus() {
        return disabled != null && ChatStatus.NULL != disabled;
    }

    public Player getBase() {
        if (cachedPlayer == null || !cachedPlayer.isOnline())
            cachedPlayer = Bukkit.getOfflinePlayer(base).getPlayer();
        return cachedPlayer;
    }

    public UUID getBaseAsUUID() {
        return base;
    }

    public boolean hasBande() {
        return bande != null;
    }

    public void setBande(String bandeName, BandeRank rank) {
        this.bande = bandeName;
        this.bandeRank = rank;
        setDirty(true);
    }

    public BandeRank getBandeRank() {
        return bandeRank;
    }

    public Bande getBande() {
        if (bande == null)
            return null;
        return BandePlugin.getAPI().getBande(bande);
    }

    public void clearInvitations() {
        this.invitations = new HashSet<>();
        setDirty(true);
    }

    public void invalidate() {
        BandePlugin.getAPI().discardCache(this);
    }

    public void updateCache() {
        BandePlugin.getAPI().addToCache(getBase(), this);
    }

    public void forceSave() {
        BandePlugin.getFileManager().savePlayer(this);
        setDirty(false);
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
                BandePlugin.getFileManager().savePlayer(BandePlayer.this);
                BandePlayer.this.invalidate();
                setDirty(false);
            }
        }
    }

}
