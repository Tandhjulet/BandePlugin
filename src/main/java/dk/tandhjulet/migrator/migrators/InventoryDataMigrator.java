package dk.tandhjulet.migrator.migrators;

import java.io.File;
import java.util.Collections;
import java.util.List;

import dk.tandhjulet.gui.InvDataHolder;
import dk.tandhjulet.migrator.IMigration;
import dk.tandhjulet.storage.FileManager;

public class InventoryDataMigrator implements IMigration {

    @Override
    @SuppressWarnings("deprecation")
    public List<File> getFiles() {
        return Collections.singletonList(FileManager.getDeprecatedInventoryDataFile());
    }

    @Override
    public Class<?> getClazz() {
        return InvDataHolder.class;
    }
}
