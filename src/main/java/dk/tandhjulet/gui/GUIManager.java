package dk.tandhjulet.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.utils.Logger;

public class GUIManager {
    private final HashMap<String, GUI> guis;

    public GUIManager() {
        this.guis = new HashMap<>();
    }

    public void reload() {
        Bukkit.getOnlinePlayers().forEach(e -> {
            e.closeInventory();
        });

        guis.clear();
        final File guiFolder = new File(BandePlugin.getPlugin().getDataFolder(), "guis");
        if (!guiFolder.exists()) {
            guiFolder.mkdirs();
        }
        if (guiFolder.listFiles().length == 0) {

            Logger.info("GUI-directory is empty... restoring from embedded.");

            BandePlugin.getPlugin().saveResource("guis.zip", false);

            new File(BandePlugin.getPlugin().getDataFolder(), "guis").mkdir();
            unzip(BandePlugin.getPlugin().getDataFolder().getAbsolutePath() + File.separator + "guis.zip",
                    BandePlugin.getPlugin().getDataFolder().getAbsolutePath() + File.separator + "guis"
                            + File.separator);

            new File(BandePlugin.getPlugin().getDataFolder(), "guis.zip").delete();
        }

        for (String gui_name : BandePlugin.getInventoryDataHolder().getIds()) {
            File f = new File(BandePlugin.getPlugin().getDataFolder(), "guis" +
                    File.separator + gui_name + ".data");
            if (!f.exists()) {
                Logger.info("Could not find gui " + gui_name + "! Creating an empty one...");
                Logger.info("You can get the pre-set of GUIs by deleting the /guis/-folder in the plugin-folder.");
            }

            addGui(gui_name, loadGUIFromFile(gui_name));
        }

    }

    private static void unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if (!dir.exists())
            dir.mkdirs();
        FileInputStream fis;
        // buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println("Opretter GUI'en \"" + fileName + "\"...");
                // create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                // close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            // close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public GUI loadGUIFromFile(String guiName) {
        GUI gui = BandePlugin.getFileManager().loadGUI(guiName);
        return gui;
    }

    public HashMap<String, GUI> getGUIs() {
        return guis;
    }

    public GUI getGUI(String id) {
        return guis.get(id);
    }

    public void openGui(final Player player, final String id, final boolean interaction, final boolean handle) {
        if (this.guis.containsKey(id)) {
            this.guis.get(id).open(player, player, interaction, handle);
        }
    }

    public void addGui(final String id, final GUI gui) {
        this.guis.put(id, gui);
    }

    public void removeGui(final String id) {
        this.guis.remove(id);
    }
}
