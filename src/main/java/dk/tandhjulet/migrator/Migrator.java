package dk.tandhjulet.migrator;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

public class Migrator {
    public static void run() {
        Set<Class<? extends IMigration>> migrationClasses = new Reflections("dk.tandhjulet.migrator.migrators")
                .getSubTypesOf(IMigration.class);

        migrationClasses.forEach(migrator -> {
            try {
                IMigration migration = migrator.newInstance();
                Map.Entry<List<File>, List<Field>> toMigrate = migration.getMigrationData();

                toMigrate.getKey().forEach(file -> {
                    migration.migrate(file);
                });

            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

}
