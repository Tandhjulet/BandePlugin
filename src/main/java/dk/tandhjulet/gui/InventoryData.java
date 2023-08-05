package dk.tandhjulet.gui;

import java.util.Collection;
import java.util.Set;

import dk.tandhjulet.config.BandeConfig;
import dk.tandhjulet.config.IConfig;
import dk.tandhjulet.config.holder.BandeHolder;
import dk.tandhjulet.config.holder.InventoryDataHolder;
import dk.tandhjulet.storage.FileManager;

public class InventoryData implements IConfig {
    private InventoryDataHolder holder;
    private BandeConfig config;

    public InventoryData() {
        config = new BandeConfig(FileManager.getInventoryDataFile());
        config.setSaveHook(() -> {
            config.setRootHolder(BandeHolder.class, holder);
        });

        reloadConfig();
        config.save();
    }

    public void setName(String id, String name) {
        holder.setName(id, name);
    }

    public Integer getSize(String id) {
        return holder.getSize(id);
    }

    public String getName(String id) {
        return holder.getName(id);
    }

    public Set<String> getIds() {
        return holder.getIds();
    }

    public Collection<String> getNames() {
        return holder.getNames();
    }

    public void setSize(String id, Integer size) {
        holder.setSize(id, size);
    }

    @Override
    public void reloadConfig() {
        config.load();
    }
}
