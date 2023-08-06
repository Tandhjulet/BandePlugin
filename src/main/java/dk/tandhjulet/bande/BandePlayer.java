package dk.tandhjulet.bande;

import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.config.BandeConfig;
import dk.tandhjulet.config.IConfig;
import dk.tandhjulet.config.holder.BandePlayerHolder;
import dk.tandhjulet.enums.BandeRank;
import dk.tandhjulet.enums.ChatStatus;
import dk.tandhjulet.migrator.Migrate;
import dk.tandhjulet.storage.FileManager;
import dk.tandhjulet.utils.Logger;

public class BandePlayer implements IConfig, Serializable {
    @Deprecated
    private static transient final long serialVersionUID = 2L;

    private BandeConfig config;
    private BandePlayerHolder holder;

    private transient boolean isDestroyed = false;

    private transient Player cachedPlayer;
    private transient ChatStatus disabled;
    private transient String previousGui;

    public BandePlayer(UUID base) throws Exception {
        final File folder = new File(BandePlugin.getPlugin().getDataFolder(), "userdata");
        if (!folder.exists() && !folder.mkdirs()) {
            throw new RuntimeException("Unable to create userdata folder!");
        }

        File userFile = FileManager.getUserFile(base);
        if (userFile.exists()) {

            config = new BandeConfig(userFile);
            config.setSaveHook(() -> {
                config.setRootHolder(BandePlayerHolder.class, holder);
            });

            this.cachedPlayer = Bukkit.getServer().getPlayer(base);
            reloadConfig();
            config.save();
        } else {
            throw new RuntimeException("Could not create Bande Player File for non-joined player!");
        }
    }

    public BandePlayer(Player base) {
        final File folder = new File(BandePlugin.getPlugin().getDataFolder(), "userdata");
        if (!folder.exists() && !folder.mkdirs()) {
            throw new RuntimeException("Unable to create userdata folder!");
        }

        config = new BandeConfig(new File(folder, base.getUniqueId() + ".yml"));
        config.setSaveHook(() -> {
            config.setRootHolder(BandePlayerHolder.class, holder);
        });

        this.cachedPlayer = base;
        reloadConfig();
        config.save();
    }

    public void destroy() {
        config.getFile().deleteOnExit();
        this.isDestroyed = true;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public String getPreviousGUI() {
        return previousGui;
    }

    public void setPreviousGUI(String id) {
        previousGui = id;
        updateCache();
    }

    public void setBandeRank(BandeRank rank) {
        holder.bandeRank(rank);
        save();
    }

    public HashSet<String> getInvitations() {
        return holder.invitations();
    }

    public void addInvitation(String bandeName) {
        HashSet<String> invitations = holder.invitations();
        invitations.add(bandeName);
        holder.invitations(invitations);

        save();
    }

    public void removeInvitation(String bandeName) {
        HashSet<String> invitations = holder.invitations();
        invitations.remove(bandeName);
        holder.invitations(invitations);

        save();
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
            cachedPlayer = Bukkit.getOfflinePlayer(holder.base()).getPlayer();
        return cachedPlayer;
    }

    public UUID getBaseAsUUID() {
        return holder.base();
    }

    public boolean hasBande() {
        return holder.bande() != null;
    }

    public void setBande(String bandeName, BandeRank rank) {
        holder.bande(bandeName);
        holder.bandeRank(rank);

        save();
    }

    public BandeRank getBandeRank() {
        return holder.bandeRank();
    }

    public Bande getBande() {
        if (holder.bande() == null)
            return null;
        return BandePlugin.getAPI().getBande(holder.bande());
    }

    public void clearInvitations() {
        holder.invitations(new HashSet<>());
        save();
    }

    public void invalidate() {
        BandePlugin.getAPI().discardCache(this);
    }

    public void updateCache() {
        BandePlugin.getAPI().addToCache(getBase(), this);
    }

    private void save() {
        config.save();
        updateCache();
    }

    @Override
    public void reloadConfig() {
        config.load();
        try {
            holder = config.getRootNode().get(BandePlayerHolder.class);
        } catch (Throwable e) {
            Logger.severe("Error while reading config: " + config.getFile().getName());
            throw new RuntimeException(e);
        }
    }

    @Migrate
    @Deprecated
    private UUID base;

    @Migrate
    @Deprecated
    private String bande;

    @Migrate
    @Deprecated
    private BandeRank bandeRank;

    @Migrate
    @Deprecated
    private HashSet<String> invitations;

}
