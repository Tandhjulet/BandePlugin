package dk.tandhjulet.migrator.migrators;

import java.io.File;
import java.util.Collections;
import java.util.List;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.migrator.IMigration;
import dk.tandhjulet.storage.FileManager;
import dk.tandhjulet.utils.Logger;

public class HouseMigrator implements IMigration {

    @Override
    @SuppressWarnings("deprecation")
    public List<File> getFiles() {
        return Collections.singletonList(FileManager.getHouseHolderFile());
    }

    @Override
    @SuppressWarnings("deprecation")
    public Class<?> getClazz() {
        return BandePlugin.getHouseHolder().getClass();
    }

    @Override
    @SuppressWarnings({ "unchecked", "deprecation" })
    public void migrate(File file) {
        try {
            BandePlugin.getConfiguration()
                    .setHouses((List<String>) getClazz().getDeclaredField("houses").get(BandePlugin.getHouseHolder()));

        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            Logger.info("[Migration] Field 'houses' is missing from house holder.");
            e.printStackTrace();
        }

        file.delete();
    }

    @Override
    public Class<?> getHolder() {
        return null;
    }
}
