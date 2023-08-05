package dk.tandhjulet.migrator.migrators;

import java.io.File;
import java.util.List;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.migrator.IMigration;

public class HouseMigrator implements IMigration {

    @Override
    public List<File> getFiles() {
        return null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public Class<?> getClazz() {
        return dk.tandhjulet.huse.HouseHolder.class;
    }

    @Override
    @SuppressWarnings({ "unchecked", "deprecation" })
    public void migrate(File file) {
        try {
            BandePlugin.getConfiguration()
                    .setHouses((List<String>) getFields().get(0).get(BandePlugin.getHouseHolder()));
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
