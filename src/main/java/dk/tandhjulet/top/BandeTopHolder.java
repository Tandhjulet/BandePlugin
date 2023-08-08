package dk.tandhjulet.top;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.config.BandeConfig;
import dk.tandhjulet.config.IConfig;
import dk.tandhjulet.config.holder.TopHolder;
import dk.tandhjulet.migrator.Migrate;
import dk.tandhjulet.storage.FileManager;
import dk.tandhjulet.utils.Logger;
import dk.tandhjulet.utils.Utils;

public class BandeTopHolder implements IConfig, Serializable {
    @Deprecated
    private static transient final long serialVersionUID = 6L;

    private BandeConfig config;
    private TopHolder holder;

    public BandeTopHolder() {
        final File folder = new File(BandePlugin.getPlugin().getDataFolder(), "data");
        if (!folder.exists() && !folder.mkdirs()) {
            throw new RuntimeException("Unable to create data folder!");
        }

        config = new BandeConfig(FileManager.getTopFile());
        config.setSaveHook(() -> {
            config.setRootHolder(TopHolder.class, holder);
        });

        reloadConfig();
        config.save();
    }

    @Deprecated
    public void init() {
        BandePlugin.scheduleSyncRepeatingTask(() -> {
            Bukkit.broadcastMessage(Utils.getColored(BandePlugin.getConfiguration().getTopPreUpdateMessage()));
            sortOffiKills();
        }, 15 * 60 * 20L);

        BandePlugin.scheduleSyncRepeatingTask(() -> {
            sortVagtKills();
        }, 15 * 60 * 20L + 1);

        BandePlugin.scheduleSyncRepeatingTask(() -> {
            sortLevels();
        }, 15 * 60 * 20L + 2);

        BandePlugin.scheduleSyncRepeatingTask(() -> {
            sortFangeKills();
            Bukkit.broadcastMessage(Utils.getColored(BandePlugin.getConfiguration().getTopAfterUpdateMessage()));
        }, 15 * 60 * 20L + 3);

        sort();
    }

    public void removeBande(String id) {
        holder.topFangeKills().remove(id);
        holder.topLevels().remove(id);
        holder.topOffiKills().remove(id);
        holder.topVagtKills().remove(id);
    }

    public void sort() {
        sortFangeKills();
        sortLevels();
        sortOffiKills();
        sortVagtKills();
    }

    // TODO: implement this
    private void sortFangeKills() {
    }

    private void sortLevels() {
    }

    private void sortOffiKills() {
    }

    private void sortVagtKills() {
    }

    public Map<String, Integer> getSortedOffiKills() {
        return null;
    }

    public Map<String, Integer> getSortedVagtKills() {
        return null;
    }

    public Map<String, Integer> getSortedLevels() {
        return null;
    }

    public Map<String, Integer> getSortedFangeKills() {
        return null;
    }

    public void addOffiKill(String bandeName) {
        holder.topOffiKills().put(bandeName, getOffiKills(bandeName) + 1);
        config.save();
    }

    public Integer getOffiKills(String bandeName) {
        return holder.topOffiKills().getOrDefault(bandeName, 0);
    }

    public void addVagtKill(String bandeName) {
        holder.topVagtKills().put(bandeName, getVagtKills(bandeName) + 1);
        config.save();
    }

    public Integer getVagtKills(String bandeName) {
        return holder.topVagtKills().getOrDefault(bandeName, 0);
    }

    public void setLevel(Integer setTo, String bandeName) {
        holder.topLevels().put(bandeName, setTo);
        config.save();
    }

    public void addFangeKill(String bandeName) {
        holder.topFangeKills().put(bandeName, getFangeKill(bandeName) + 1);
        config.save();
    }

    public Integer getFangeKill(String bandeName) {
        return holder.topFangeKills().getOrDefault(bandeName, 0);
    }

    @Override
    public void reloadConfig() {
        config.load();

        try {
            holder = config.getRootNode().get(TopHolder.class);
        } catch (Throwable e) {
            Logger.severe("Error while reading config: " + config.getFile().getName());
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    @Migrate
    private HashMap<String, Integer> topOffiKills;

    @Deprecated
    @Migrate
    private HashMap<String, Integer> topVagtKills;

    @Deprecated
    @Migrate
    private HashMap<String, Integer> topLevel;

    @Deprecated
    @Migrate
    private HashMap<String, Integer> topFangeKills;
}
