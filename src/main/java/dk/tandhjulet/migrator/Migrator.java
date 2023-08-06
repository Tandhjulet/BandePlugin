package dk.tandhjulet.migrator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dk.tandhjulet.migrator.migrators.BandeMigrator;
import dk.tandhjulet.migrator.migrators.GUIMigrator;
import dk.tandhjulet.migrator.migrators.HouseMigrator;
import dk.tandhjulet.migrator.migrators.InventoryDataMigrator;
import dk.tandhjulet.migrator.migrators.TopMigrator;
import dk.tandhjulet.migrator.migrators.UserMigrator;
import dk.tandhjulet.utils.Logger;

public class Migrator {
    private static Set<Class<? extends IMigration>> migrationClasses = new HashSet<>(
            Arrays.asList(BandeMigrator.class, GUIMigrator.class, HouseMigrator.class, InventoryDataMigrator.class,
                    TopMigrator.class, UserMigrator.class));

    public static void run() {
        migrationClasses.forEach(migrator -> {
            try {
                IMigration migration = migrator.newInstance();
                if (migration.shouldMigrate()) {
                    Logger.info("Migrating... now running: " + migration.getClass().getName());

                    Map.Entry<List<File>, List<Field>> toMigrate = migration.getMigrationData();

                    toMigrate.getKey().forEach(file -> {
                        try {
                            migration.migrate(file);
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    });
                }

            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                Logger.severe("A severe error occured whilst migrating...");
            }
        });
    }
}
