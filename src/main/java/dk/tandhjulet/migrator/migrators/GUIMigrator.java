package dk.tandhjulet.migrator.migrators;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import dk.tandhjulet.config.holder.GUIHolder;
import dk.tandhjulet.gui.GUI;
import dk.tandhjulet.migrator.IMigration;
import dk.tandhjulet.storage.FileManager;

public class GUIMigrator implements IMigration {

    @Override
    public List<File> getFiles() {
        return Arrays.asList(FileManager.getGUIFile("").getParentFile().listFiles());
    }

    @Override
    public Class<?> getClazz() {
        return GUI.class;
    }

    @Override
    public Class<?> getHolder() {
        return GUIHolder.class;
    }
}
