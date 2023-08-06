package dk.tandhjulet.migrator.migrators;

import java.io.File;
import java.util.Collections;
import java.util.List;

import dk.tandhjulet.config.holder.TopHolder;
import dk.tandhjulet.migrator.IMigration;
import dk.tandhjulet.storage.FileManager;
import dk.tandhjulet.top.BandeTop;

public class TopMigrator implements IMigration {

    @Override
    public List<File> getFiles() {
        return Collections.singletonList(FileManager.getTopFile());
    }

    @Override
    public Class<?> getClazz() {
        return BandeTop.class;
    }

    @Override
    public Class<?> getHolder() {
        return TopHolder.class;
    }
}
