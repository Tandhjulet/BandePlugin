package dk.tandhjulet.top;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;

import com.google.common.collect.ImmutableSortedMap;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.utils.Utils;

public class BandeTopHolder implements Serializable {
    private static transient final long serialVersionUID = 6L;
    public transient boolean dirty;
    private transient Timer timer;

    private HashMap<String, Integer> topOffiKills;
    private transient Map<String, Integer> sortedOffiKills;

    private HashMap<String, Integer> topVagtKills;
    private transient Map<String, Integer> sortedVagtKills;

    private HashMap<String, Integer> topLevel;
    private transient Map<String, Integer> sortedLevels;

    private HashMap<String, Integer> topFangeKills;
    private transient Map<String, Integer> sortedFangeKills;

    public BandeTopHolder() {
        topOffiKills = new HashMap<>();
        topVagtKills = new HashMap<>();
        topLevel = new HashMap<>();
        topFangeKills = new HashMap<>();
    }

    public void init() {
        timer = new Timer("Bande Top");
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (isDirty()) {

                    setDirty(false);
                    BandePlugin.getFileManager().saveTop(BandeTopHolder.this);
                }
            }

        }, 30 * 1000, 30 * 1000); // save every 30th second.

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

        sortFangeKills();
        sortLevels();
        sortOffiKills();
        sortVagtKills();
    }

    public void removeBande(String id) {
        topFangeKills.remove(id);
        topLevel.remove(id);
        topOffiKills.remove(id);
        topVagtKills.remove(id);
    }

    public Map<String, Integer> getSortedOffiKills() {
        if (sortedOffiKills == null) {
            return ImmutableSortedMap.of();
        }
        return sortedOffiKills;
    }

    public Map<String, Integer> getSortedVagtKills() {
        if (sortedVagtKills == null) {
            return ImmutableSortedMap.of();
        }
        return sortedVagtKills;
    }

    public Map<String, Integer> getSortedLevels() {
        if (sortedLevels == null) {
            return ImmutableSortedMap.of();
        }
        return sortedLevels;
    }

    public Map<String, Integer> getSortedFangeKills() {
        if (sortedFangeKills == null) {
            return ImmutableSortedMap.of();
        }
        return sortedFangeKills;
    }

    public void sortOffiKills() {
        sortedOffiKills = topOffiKills.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o, n) -> o, LinkedHashMap::new));
    }

    public void sortVagtKills() {
        sortedVagtKills = topVagtKills.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o, n) -> o, LinkedHashMap::new));
    }

    public void sortLevels() {
        sortedLevels = topLevel.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o, n) -> o, LinkedHashMap::new));
    }

    public void sortFangeKills() {
        sortedFangeKills = topFangeKills.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o, n) -> o, LinkedHashMap::new));
    }

    public void addOffiKill(String bandeName) {
        topOffiKills.put(bandeName, getOffiKills(bandeName) + 1);
        save();
    }

    public Integer getOffiKills(String bandeName) {
        return topOffiKills.getOrDefault(bandeName, 0);
    }

    public void addVagtKill(String bandeName) {
        topVagtKills.put(bandeName, getVagtKills(bandeName) + 1);
        save();
    }

    public Integer getVagtKills(String bandeName) {
        return topVagtKills.getOrDefault(bandeName, 0);
    }

    public void setLevel(Integer setTo, String bandeName) {
        topLevel.put(bandeName, setTo);
        save();
    }

    public void addFangeKill(String bandeName) {
        topFangeKills.put(bandeName, getFangeKill(bandeName) + 1);
        setDirty(true);

    }

    public Integer getFangeKill(String bandeName) {
        return topFangeKills.getOrDefault(bandeName, 0);
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void save() {
        BandePlugin.getFileManager().saveTop(this);
    }
}
