package dk.tandhjulet.top;

import java.util.Map;

import org.bukkit.Bukkit;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.config.BandeConfig;
import dk.tandhjulet.config.IConfig;
import dk.tandhjulet.config.holder.TopHolder;
import dk.tandhjulet.storage.FileManager;
import dk.tandhjulet.utils.Utils;

public class BandeTop implements IConfig {
    private BandeConfig config;
    private TopHolder holder;

    public BandeTop() {
        config = new BandeConfig(FileManager.getTopFile());
        config.setSaveHook(() -> {
            config.setRootHolder(TopHolder.class, holder);
        });

        reloadConfig();
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
        holder.fangeKills().remove(id);
        holder.bandeLevels().remove(id);
        holder.offiKills().remove(id);
        holder.vagtKills().remove(id);
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
        holder.offiKills().put(bandeName, getOffiKills(bandeName) + 1);
        config.save();
    }

    public Integer getOffiKills(String bandeName) {
        return holder.offiKills().getOrDefault(bandeName, 0);
    }

    public void addVagtKill(String bandeName) {
        holder.vagtKills().put(bandeName, getVagtKills(bandeName) + 1);
        config.save();
    }

    public Integer getVagtKills(String bandeName) {
        return holder.vagtKills().getOrDefault(bandeName, 0);
    }

    public void setLevel(Integer setTo, String bandeName) {
        holder.bandeLevels().put(bandeName, setTo);
        config.save();
    }

    public void addFangeKill(String bandeName) {
        holder.fangeKills().put(bandeName, getFangeKill(bandeName) + 1);
        config.save();
    }

    public Integer getFangeKill(String bandeName) {
        return holder.fangeKills().getOrDefault(bandeName, 0);
    }

    @Override
    public void reloadConfig() {
        config.load();
    }
}
