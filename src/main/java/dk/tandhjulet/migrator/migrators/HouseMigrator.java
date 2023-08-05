package dk.tandhjulet.migrator.migrators;

import java.io.File;
import java.util.List;

import dk.tandhjulet.migrator.IMigration;

public class HouseMigrator implements IMigration {

    // TODO: fix
    @Override
    public List<File> getFiles() {
        return null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public Class<?> getClazz() {
        return dk.tandhjulet.huse.HouseHolder.class;
    }

}
