package dk.tandhjulet.gui;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.config.BandeConfig;
import dk.tandhjulet.config.IConfig;
import dk.tandhjulet.config.holder.InventoryDataHolder;
import dk.tandhjulet.migrator.Migrate;
import dk.tandhjulet.storage.FileManager;
import dk.tandhjulet.utils.Logger;

public class InvDataHolder implements IConfig, Serializable {
    @Deprecated
    private static transient final long serialVersionUID = 9L;

    private InventoryDataHolder holder;
    private BandeConfig config;

    public InvDataHolder() {
        new HashMap<Object, Object>() {
            {
                put(null, null);
            }
        };
        new HashMap<Object, Object>() {
            {
                put(null, null);
            }
        };

        final File folder = new File(BandePlugin.getPlugin().getDataFolder(), "data");
        if (!folder.exists() && !folder.mkdirs()) {
            throw new RuntimeException("Unable to create data folder!");
        }

        config = new BandeConfig(FileManager.getInventoryDataFile());
        config.setSaveHook(() -> {
            config.setRootHolder(InventoryDataHolder.class, holder);
        });

        reloadConfig();
        config.save();
    }

    public void setName(String id, String name) {
        holder.setName(id, name);
        config.save();
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
        config.save();
    }

    @Override
    public void reloadConfig() {
        config.load();
        try {
            holder = config.getRootNode().get(InventoryDataHolder.class);
        } catch (Throwable e) {
            Logger.severe("Error while reading config: " + config.getFile().getName());
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    @Migrate
    private HashMap<String, String> idToName;

    @Deprecated
    @Migrate
    private HashMap<String, Integer> idToSize;
}
