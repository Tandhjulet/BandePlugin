package dk.tandhjulet.migrator.migrators;

import java.io.File;
import java.util.Collections;
import java.util.List;

import dk.tandhjulet.migrator.IMigration;
import dk.tandhjulet.storage.FileManager;
import dk.tandhjulet.top.BandeTopHolder;

public class TopMigrator implements IMigration {

    @Override
    @SuppressWarnings("deprecation")
    public List<File> getFiles() {
        return Collections.singletonList(FileManager.getDeprecatedTopFile());
    }

    @Override
    public Class<?> getClazz() {
        return BandeTopHolder.class;
    }
}
