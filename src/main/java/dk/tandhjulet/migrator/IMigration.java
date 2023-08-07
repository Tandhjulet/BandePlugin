package dk.tandhjulet.migrator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.spongepowered.configurate.serialize.SerializationException;

import dk.tandhjulet.config.BandeConfig;
import dk.tandhjulet.config.holder.GUIHolder;
import dk.tandhjulet.gui.GUIItem;
import dk.tandhjulet.migrator.migrators.GUIMigrator;
import dk.tandhjulet.storage.FileManager;
import dk.tandhjulet.utils.Logger;

public interface IMigration {
    public default Map.Entry<List<File>, List<Field>> getMigrationData() {
        List<Field> fields = getFields();
        List<File> files = getFiles();

        return new AbstractMap.SimpleEntry<List<File>, List<Field>>(files, fields);
    }

    @SuppressWarnings("unchecked")
    public default void exchangeFields(BandeConfig config, Object extractFrom,
            Map.Entry<List<File>, List<Field>> toMigrate) {
        if (extractFrom == null) {
            return;
        }

        toMigrate.getValue().forEach(field -> {
            try {
                field.setAccessible(true);

                Object extractedValue = field.get(extractFrom);

                if (extractedValue instanceof Map<?, ?>) {
                    HashMap<?, ?> map = (HashMap<?, ?>) extractedValue;
                    if (!map.isEmpty()) {
                        Map.Entry<?, ?> typesToExtract = map.entrySet().iterator().next();
                        if (typesToExtract.getKey() instanceof Integer
                                && typesToExtract.getValue() instanceof GUIItem
                                && this instanceof GUIMigrator) {

                            // horrible code, but it works! :D

                            GUIHolder holder = config.getRootNode().get(GUIHolder.class);
                            config.setSaveHook(() -> {
                                config.setRootHolder(GUIHolder.class, holder);
                            });

                            holder.contents((HashMap<Integer, GUIItem>) map);

                        } else {
                            config.setUnsafeProperty(field.getName(), extractedValue);
                        }
                    }
                } else {
                    config.setUnsafeProperty(field.getName(), extractedValue);
                }
            } catch (IllegalArgumentException | IllegalAccessException | NullPointerException
                    | SerializationException e) {
                Logger.severe("Lost data during migration... continuing.");
                e.printStackTrace();
            }
        });
    }

    public default void migrate(File file) throws IOException, ClassNotFoundException {
        if (!file.getName().endsWith(".data"))
            return;

        File newFile = new File(file.getParentFile(), file.getName().replace(".data", ".yml"));
        BandeConfig config = new BandeConfig(newFile);
        config.load();

        Object toExtract = FileManager.loadDeprecated(file);
        Map.Entry<List<File>, List<Field>> toMigrate = getMigrationData();

        exchangeFields(config, toExtract, toMigrate);

        config.save();
        file.delete();
    }

    public default boolean shouldMigrate() {
        AtomicBoolean shouldMigrate = new AtomicBoolean(false);

        List<File> files = getFiles();
        if (files.isEmpty())
            return false;

        files.forEach(file -> {
            File f = new File(file.getParentFile(), file.getName().replace(".yml", ".data"));
            if (f.exists()) {
                shouldMigrate.set(true);
            }
        });

        return shouldMigrate.get();
    }

    public default List<Field> getFields() {
        Class<?> clazz = getClazz();
        List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));

        fields.removeIf(field -> !field.isAnnotationPresent(Migrate.class));
        return fields;
    }

    public List<File> getFiles();

    public Class<?> getClazz();

    public Class<?> getHolder();
}
