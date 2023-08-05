package dk.tandhjulet.gui;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.storage.FileManager;
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

        for (String gui_name : BandePlugin.getInventoryDataHolder().getIds()) {
            File f = new File(BandePlugin.getPlugin().getDataFolder(), "guis" +
                    File.separator + gui_name + ".yml");
            if (!f.exists()) {
                Logger.info("Could not find gui " + gui_name + "! Creating an empty one...");
            }

            addGui(gui_name, loadGUIFromFile(gui_name));
        }

    }

    public GUI loadGUIFromFile(String guiName) {
        GUI gui = FileManager.loadYAMLGUI(guiName);
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
