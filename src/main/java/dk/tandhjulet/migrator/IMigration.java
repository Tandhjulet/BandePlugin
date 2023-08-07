package dk.tandhjulet.migrator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.gson.reflect.TypeToken;

import dk.tandhjulet.config.BandeConfig;
import dk.tandhjulet.gui.GUIItem;
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
                Logger.info(field.getType().toString());

                if (extractedValue instanceof Map<?, ?>) {
                    Map<?, ?> map = (Map<?, ?>) extractedValue;
                    if (!map.isEmpty()) {
                        Map.Entry<?, ?> typesToExtract = map.entrySet().iterator().next();
                        if (typesToExtract.getKey() instanceof Integer
                                && typesToExtract.getValue() instanceof GUIItem) {

                            config.setMap(field.getName(), (Map<Integer, GUIItem>) map,
                                    new TypeToken<Map<Integer, GUIItem>>() {
                                    }.getType());

                        } else {
                            config.setUnsafeProperty(field.getName(), extractedValue);
                        }
                    }
                } else {
                    config.setUnsafeProperty(field.getName(), extractedValue);
                }
            } catch (IllegalArgumentException | IllegalAccessException | NullPointerException e) {
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
        getFiles().forEach(file -> {
            if (new File(file.getParentFile(), file.getName().replace(".yml", ".data")).exists()) {
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
