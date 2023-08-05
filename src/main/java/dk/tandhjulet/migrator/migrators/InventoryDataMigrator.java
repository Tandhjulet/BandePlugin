package dk.tandhjulet.migrator.migrators;

import java.io.File;
import java.util.Collections;
import java.util.List;

import dk.tandhjulet.config.holder.InventoryDataHolder;
import dk.tandhjulet.migrator.IMigration;
import dk.tandhjulet.storage.FileManager;

public class InventoryDataMigrator implements IMigration {

    @Override
    public List<File> getFiles() {
        return Collections.singletonList(FileManager.getInventoryDataFile());
    }

    @Override
    public Class<?> getClazz() {
        return InventoryDataHolder.class;
    }

}
