package dk.tandhjulet.migrator;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import dk.tandhjulet.config.BandeConfig;
import dk.tandhjulet.storage.FileManager;

public class Migrator {
    public static void run() {
        Set<Class<? extends IMigration>> migrationClasses = new Reflections("dk.tandhjulet.migrator.migrators")
                .getSubTypesOf(IMigration.class);

        migrationClasses.forEach(migrator -> {
            try {
                IMigration migration = migrator.newInstance();
                Map.Entry<List<File>, List<Field>> toMigrate = migration.migrate();

                toMigrate.getKey().forEach(file -> {

                    BandeConfig config = new BandeConfig(file);
                    Object toExtract = FileManager.loadDeprecated(file);

                    toMigrate.getValue().forEach(field -> {
                        try {
                            config.setUnsafeProperty(field.getName(),
                                    field.get(toExtract));
                        } catch (IllegalArgumentException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    });

                    config.save();
                });

            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

}
