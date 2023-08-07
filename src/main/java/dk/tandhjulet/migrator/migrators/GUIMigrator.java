package dk.tandhjulet.migrator.migrators;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dk.tandhjulet.config.holder.GUIHolder;
import dk.tandhjulet.gui.GUI;
import dk.tandhjulet.migrator.IMigration;
import dk.tandhjulet.storage.FileManager;

public class GUIMigrator implements IMigration {

    @Override
    public List<File> getFiles() {
        File[] files = FileManager.getGUIFile("").getParentFile().listFiles();
        if (files == null || files.length == 0)
            return new ArrayList<>();
        return Arrays.asList(files);
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
