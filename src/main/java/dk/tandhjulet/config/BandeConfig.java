package dk.tandhjulet.config;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Location;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.ParsingException;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import dk.tandhjulet.config.annotations.DeleteOnEmpty;
import dk.tandhjulet.config.processors.DeleteOnEmptyProcessor;
import dk.tandhjulet.config.serializers.BigDecimalSerializer;
import dk.tandhjulet.config.serializers.GUIItemSerializer;
import dk.tandhjulet.config.serializers.UUIDSerializer;
import dk.tandhjulet.gui.GUIItem;
import dk.tandhjulet.utils.Logger;

public class BandeConfig {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
    private final AtomicInteger pendingWrites = new AtomicInteger(0);

    private Runnable saveHook;

    private final YamlConfigurationLoader loader;
    private CommentedConfigurationNode configurationNode;
    private static final ObjectMapper.Factory MAPPER_FACTORY = ObjectMapper.factoryBuilder()
            .addProcessor(DeleteOnEmpty.class, (data, value) -> new DeleteOnEmptyProcessor())
            .build();
    private static final TypeSerializerCollection SERIALIZERS = TypeSerializerCollection.defaults().childBuilder()
            .registerAnnotatedObjects(MAPPER_FACTORY)
            .register(GUIItem.class, new GUIItemSerializer())
            .register(UUID.class, new UUIDSerializer())
            .register(BigDecimal.class, new BigDecimalSerializer())
            .build();
    File configFile;

    public BandeConfig(final File configFile) {
        this.configFile = configFile;
        this.loader = YamlConfigurationLoader.builder()
                .nodeStyle(NodeStyle.BLOCK)
                .defaultOptions(opts -> opts.serializers(SERIALIZERS))
                .indent(2)
                .file(configFile)
                .build();
    }

    public CommentedConfigurationNode getRootNode() {
        return configurationNode;
    }

    public void setRootHolder(final Class<?> holderClass, final Object holder) {
        try {
            getRootNode().set(holderClass, holder);
        } catch (SerializationException e) {
            Logger.severe("Error while saving user config: " + configFile.getName());
            throw new RuntimeException(e);
        }
    }

    public void setProperty(final String path, final long value) {
        setInternal(path, value);
    }

    public void setProperty(final String path, final int value) {
        setInternal(path, value);
    }

    public void setProperty(final String path, final double value) {
        setInternal(path, value);
    }

    public void setProperty(final String path, final float value) {
        setInternal(path, value);
    }

    public void setProperty(final String path, final List<?> value) {
        setInternal(path, value);
    }

    public void setProperty(final String path, final Location value) {
        setInternal(path, value);
    }

    public void setProperty(final String path, final String value) {
        setInternal(path, value);
    }

    public void setProperty(final String path, final boolean value) {
        setInternal(path, value);
    }

    public void setUnsafeProperty(final String path, final Object value) {
        setInternal(path, value);
    }

    public File getFile() {
        return configFile;
    }

    public <T> List<T> getList(final String path, Class<T> type) {
        final CommentedConfigurationNode node = getInternal(path);
        if (node == null) {
            return new ArrayList<>();
        }
        try {
            final List<T> list = node.getList(type);
            if (list == null) {
                return new ArrayList<>();
            }
            return list;
        } catch (SerializationException e) {
            Logger.severe(e.getMessage());
            return new ArrayList<>();
        }
    }

    public double getDouble(final String path, final double def) {
        final CommentedConfigurationNode node = getInternal(path);
        if (node == null) {
            return def;
        }
        return node.getDouble();
    }

    public String getString(final String path, final String def) {
        final CommentedConfigurationNode node = getInternal(path);
        if (node == null) {
            return def;
        }
        return node.getString();
    }

    public Map<String, String> getStringMap(String path) {
        final CommentedConfigurationNode node = getInternal(path);
        if (node == null || !node.isMap()) {
            return Collections.emptyMap();
        }

        final Map<String, String> map = new LinkedHashMap<>();
        for (Map.Entry<Object, CommentedConfigurationNode> entry : node.childrenMap().entrySet()) {
            map.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue().rawScalar()));
        }
        return map;
    }

    public Map<Object, CommentedConfigurationNode> getMap(String path) {
        final CommentedConfigurationNode node = getInternal(path);
        return node.childrenMap();
    }

    public void removeProperty(String path) {
        final CommentedConfigurationNode node = getInternal(path);
        if (node != null) {
            try {
                node.set(null);
            } catch (SerializationException e) {
                Logger.severe(e.getMessage());
            }
        }
    }

    public float getFloat(final String path, final float def) {
        final CommentedConfigurationNode node = getInternal(path);
        if (node == null) {
            return def;
        }
        return node.getFloat();
    }

    public int getInt(final String path, final int def) {
        final CommentedConfigurationNode node = getInternal(path);
        if (node == null) {
            return def;
        }
        return node.getInt();
    }

    public long getLong(final String path, final long def) {
        final CommentedConfigurationNode node = getInternal(path);
        if (node == null) {
            return def;
        }
        return node.getLong();
    }

    public boolean getBoolean(final String path, final boolean def) {
        final CommentedConfigurationNode node = getInternal(path);
        if (node == null) {
            return def;
        }
        return node.getBoolean();
    }

    private void setInternal(final String path, final Object value) {
        try {
            toSplitRoot(path, configurationNode).set(value);
        } catch (SerializationException e) {
            Logger.severe(e.getMessage());
        }
    }

    private CommentedConfigurationNode getInternal(final String path) {
        final CommentedConfigurationNode node = toSplitRoot(path, configurationNode);
        if (node.virtual()) {
            return null;
        }
        return node;
    }

    public CommentedConfigurationNode getSection(final String path) {
        final CommentedConfigurationNode node = toSplitRoot(path, configurationNode);
        if (node.virtual())
            return null;
        return node;
    }

    public Set<String> getKeys() {
        return ConfigurateUtil.getKeys(configurationNode);
    }

    public Object get(final String path) {
        final CommentedConfigurationNode node = getInternal(path);
        return node == null ? null : node.raw();
    }

    public CommentedConfigurationNode toSplitRoot(String path, final CommentedConfigurationNode node) {
        if (path == null) {
            return node;
        }
        path = path.startsWith(".") ? path.substring(1) : path;
        return node.node(path.contains(".") ? path.split("\\.") : new Object[] { path });
    }

    public void load() {
        if (pendingWrites.get() != 0) {
            Logger.info(
                    "Parsing config file " + configFile + " has been aborted due to " + pendingWrites.get()
                            + " current pending write(s).");
            return;
        }

        try {
            configurationNode = loader.load();
        } catch (final ParsingException e) {
            final File broken = new File(configFile.getAbsolutePath() + ".broken." + System.currentTimeMillis());
            if (configFile.renameTo(broken)) {
                Logger.severe(
                        "File " + configFile + " is broken. It has been backed up and renamed to " + broken + "!");
                return;
            }
            Logger.severe("File " + configFile + " is broken. Backup file couldn't be created.");
        } catch (ConfigurateException e) {
            Logger.info(e.getMessage());
        } finally {
            if (configurationNode == null) {
                configurationNode = loader.createNode();
            }
        }
    }

    public void setSaveHook(Runnable saveHook) {
        this.saveHook = saveHook;
    }

    public synchronized void save() {
        delaySave();
    }

    private Future<?> delaySave() {
        if (saveHook != null) {
            saveHook.run();
        }

        final CommentedConfigurationNode node = configurationNode.copy();
        pendingWrites.incrementAndGet();

        return EXECUTOR_SERVICE.submit(new ConfigSaveTask(loader, node, pendingWrites));
    }
}
