package dk.tandhjulet.migrator.migrators;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.config.holder.BandeHolder;
import dk.tandhjulet.migrator.IMigration;
import dk.tandhjulet.storage.FileManager;

public class BandeMigrator implements IMigration {

    public List<File> getFiles() {
        return Arrays.asList(FileManager.getBandeFile("").getParentFile().listFiles());
    }

    @Override
    public Class<?> getClazz() {
        return Bande.class;
    }

    @Override
    public Class<?> getHolder() {
        return BandeHolder.class;
    }

}
