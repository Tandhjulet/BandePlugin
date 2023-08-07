package dk.tandhjulet.migrator.migrators;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.config.holder.BandeHolder;
import dk.tandhjulet.migrator.IMigration;
import dk.tandhjulet.storage.FileManager;

public class BandeMigrator implements IMigration {

    public List<File> getFiles() {
        File[] files = FileManager.getBandeFile("").getParentFile().listFiles();
        if (files == null || files.length == 0)
            return new ArrayList<>();
        return Arrays.asList(files);
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
