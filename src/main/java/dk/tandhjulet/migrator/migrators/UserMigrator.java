package dk.tandhjulet.migrator.migrators;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.migrator.IMigration;
import dk.tandhjulet.storage.FileManager;

public class UserMigrator implements IMigration {

    @Override
    public List<File> getFiles() {
        File[] files = FileManager.getUserFile(UUID.randomUUID()).getParentFile().listFiles();
        if (files == null || files.length == 0)
            return new ArrayList<>();
        return Arrays.asList(files);
    }

    @Override
    public Class<?> getClazz() {
        return BandePlayer.class;
    }
}
