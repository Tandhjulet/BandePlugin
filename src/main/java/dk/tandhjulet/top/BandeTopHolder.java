package dk.tandhjulet.top;

import java.io.File;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.config.BandeConfig;
import dk.tandhjulet.config.IConfig;
import dk.tandhjulet.config.holder.TopHolder;
import dk.tandhjulet.enums.Sort;
import dk.tandhjulet.migrator.Migrate;
import dk.tandhjulet.storage.FileManager;
import dk.tandhjulet.utils.Logger;
import dk.tandhjulet.utils.Utils;

public class BandeTopHolder implements IConfig, Serializable {
    @Deprecated
    private static transient final long serialVersionUID = 6L;

    private BandeConfig config;
    private TopHolder holder;

    private Map<String, Integer> sortedFangeKills = new HashMap<>();
    private Map<String, Integer> sortedVagtKills = new HashMap<>();
    private Map<String, Integer> sortedOffiKills = new HashMap<>();
    private Map<String, Integer> sortedLevels = new HashMap<>();

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

        init();
    }

    public void init() {
        BandePlugin.scheduleSyncRepeatingTask(() -> {
            Bukkit.broadcastMessage(Utils.getColored(BandePlugin.getConfiguration().getTopPreUpdateMessage()));
            sort(Sort.OFFICER_KILLS);
        }, 15 * 60 * 20L);

        BandePlugin.scheduleSyncRepeatingTask(() -> {
            sort(Sort.VAGT_KILLS);
        }, 15 * 60 * 20L + 1);

        BandePlugin.scheduleSyncRepeatingTask(() -> {
            sort(Sort.LEVELS);
        }, 15 * 60 * 20L + 2);

        BandePlugin.scheduleSyncRepeatingTask(() -> {
            sort(Sort.FANGE_KILLS);
            Bukkit.broadcastMessage(Utils.getColored(BandePlugin.getConfiguration().getTopAfterUpdateMessage()));
        }, 15 * 60 * 20L + 3);

        sort(Sort.ALL);
    }

    public void removeBande(String id) {
        holder.topFangeKills().remove(id);
        holder.topLevels().remove(id);
        holder.topOffiKills().remove(id);
        holder.topVagtKills().remove(id);

        config.save();
    }

    public void sort(Sort toSort) {
        switch (toSort) {
            case FANGE_KILLS:
                sortedFangeKills = sort(holder.topFangeKills());
                break;
            case LEVELS:
                sortedLevels = sort(holder.topLevels());
                break;
            case OFFICER_KILLS:
                sortedOffiKills = sort(holder.topOffiKills());
                break;
            case VAGT_KILLS:
                sortedVagtKills = sort(holder.topVagtKills());
                break;
            case ALL:
                sortedFangeKills = sort(holder.topFangeKills());
                sortedLevels = sort(holder.topLevels());
                sortedOffiKills = sort(holder.topOffiKills());
                sortedVagtKills = sort(holder.topVagtKills());
        }
    }

    private Map<String, Integer> sort(final Map<String, Integer> toSort) {
        return toSort.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    public Map<String, Integer> getSortedOffiKills() {
        return sortedOffiKills;
    }

    public Map<String, Integer> getSortedVagtKills() {
        return sortedVagtKills;
    }

    public Map<String, Integer> getSortedLevels() {
        return sortedLevels;
    }

    public Map<String, Integer> getSortedFangeKills() {
        return sortedFangeKills;
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
