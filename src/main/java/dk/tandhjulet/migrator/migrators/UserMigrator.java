package dk.tandhjulet.migrator.migrators;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.config.holder.BandePlayerHolder;
import dk.tandhjulet.migrator.IMigration;
import dk.tandhjulet.storage.FileManager;

public class UserMigrator implements IMigration {

    @Override
    public List<File> getFiles() {
        return Arrays.asList(FileManager.getUserFile(UUID.randomUUID()).getParentFile().listFiles());
    }

    @Override
    public Class<?> getClazz() {
        return BandePlayer.class;
    }

    @Override
    public Class<?> getHolder() {
        return BandePlayerHolder.class;
    }
}
