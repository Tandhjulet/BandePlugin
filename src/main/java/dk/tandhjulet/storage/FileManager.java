package dk.tandhjulet.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.gui.GUI;
import dk.tandhjulet.gui.InventoryData;
import dk.tandhjulet.top.BandeTop;

public class FileManager {
    private static String BANDE_PATH = "bander";
    private static String GUI_PATH = "guis";
    private static String PLAYER_PATH = "players";
    private static String BANDE_TOP_PATH = "data";
    private static String BANDE_HOUSE_HOLDER_PATH = "houses";

    private static String fileBase = BandePlugin.getPlugin().getDataFolder().getPath();

    public static Object loadDeprecated(File toLoad) {
        try {
            BukkitObjectInputStream in = new BukkitObjectInputStream(
                    new GZIPInputStream(
                            new FileInputStream(
                                    toLoad)));
            Object data = in.readObject();
            in.close();
            return data;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BandePlayer loadUncachedUser(final Player base) {
        if (base == null)
            return null;

        BandePlayer player = BandePlugin.getAPI().getIfPresent(base);
        if (player == null) {
            player = new BandePlayer(base);
            BandePlugin.getAPI().addToCache(base, player);
        }
        return player;
    }

    public static BandePlayer getUser(final Player base) {
        final BandePlayer player = loadUncachedUser(base);
        BandePlugin.getAPI().addToCache(base, player);
        return player;
    }

    public static GUI loadYAMLGUI(final String name) {
        if (name == null) {
            return null;
        }

        GUI gui;
        if ((gui = BandePlugin.getGuiManager().getGUI(name)) != null) {
            return gui;
        }

        return new GUI(name, BandePlugin.getInventoryDataHolder().getSize(name));
    }

    public static BandePlayer loadUncachedUser(final UUID uuid) {
        Player player = Bukkit.getServer().getPlayer(uuid);

        BandePlayer user;
        if ((user = BandePlugin.getAPI().getIfPresent(player)) != null) {
            return user;
        }

        if (player != null) {
            user = new BandePlayer(player);
            BandePlugin.getAPI().addToCache(player, user);
            return user;
        }

        try {
            user = new BandePlayer(uuid);
            BandePlugin.getAPI().addToCache(player, user);
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    public static Bande loadUncachedBande(final String name) {

        Bande bande;
        if ((bande = BandePlugin.getAPI().getIfPresent(name)) != null) {
            return bande;
        }

        if (name != null) {
            bande = new Bande(name, null);
            BandePlugin.getAPI().addToCache(name, bande);
            return bande;
        }
        return null;
    }

    public static Bande getBande(final String name) {
        final Bande bande = loadUncachedBande(name);
        BandePlugin.getAPI().addToCache(name, bande);
        return bande;
    }

    @Deprecated
    public FileManager(String fileBase) {
        File base = new File(fileBase);

        File bande = new File(fileBase, BANDE_PATH);
        File guis = new File(fileBase, GUI_PATH);
        File players = new File(fileBase, PLAYER_PATH);
        File top = new File(fileBase, BANDE_TOP_PATH);
        File holder = new File(fileBase, BANDE_HOUSE_HOLDER_PATH);

        if (!base.exists()) {
            base.mkdirs();
        }

        if (!bande.exists()) {
            bande.mkdirs();
        }

        if (!guis.exists()) {
            guis.mkdirs();
        }

        if (!players.exists()) {
            players.mkdirs();
        }

        if (!top.exists()) {
            top.mkdirs();
        }

        if (!holder.exists()) {
            holder.mkdirs();
        }
    }

    @Deprecated
    public Bande loadBande(String bandeName) {
        if (!bandeExists(bandeName)) {
            return null;
        }

        try {
            BukkitObjectInputStream in = new BukkitObjectInputStream(
                    new GZIPInputStream(
                            new FileInputStream(
                                    fileBase + File.separator + BANDE_PATH + File.separator + bandeName + ".data")));
            Bande data = (Bande) in.readObject();
            in.close();
            return data;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Deprecated
    public boolean saveBande(String bandeName, Bande bande) {
        try {
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(
                    new GZIPOutputStream(
                            new FileOutputStream(
                                    fileBase + File.separator + BANDE_PATH + File.separator + bandeName + ".data")));
            out.writeObject(bande);
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Deprecated
    public void removeBande(String bandeName) {
        File f = new File(fileBase + File.separator + BANDE_PATH + File.separator + bandeName + ".data");
        f.delete();
    }

    @Deprecated
    public GUI loadGUI(String guiName) {
        if (!guiExists(guiName)) {
            GUI gui = new GUI(guiName);
            gui.init();
            saveGUI(guiName, gui);
            return gui;
        }

        try {
            BukkitObjectInputStream in = new BukkitObjectInputStream(
                    new GZIPInputStream(
                            new FileInputStream(
                                    fileBase + File.separator + GUI_PATH + File.separator + guiName + ".data")));
            GUI data = (GUI) in.readObject();
            data.init();
            in.close();
            return data;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Deprecated
    public boolean saveGUI(String guiName, GUI gui) {
        try {
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(
                    new GZIPOutputStream(
                            new FileOutputStream(
                                    fileBase + File.separator + GUI_PATH + File.separator + guiName + ".data")));
            out.writeObject(gui);
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Deprecated
    public BandePlayer loadPlayer(UUID uuid) {

        if (!playerExists(uuid.toString())) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

            if (player == null || !player.isOnline() || player.getPlayer() == null) {
                return null;
            }

            BandePlayer bandePlayer = new BandePlayer(player.getPlayer());
            savePlayer(bandePlayer);
            return bandePlayer;
        }

        try {
            BukkitObjectInputStream in = new BukkitObjectInputStream(
                    new GZIPInputStream(
                            new FileInputStream(
                                    fileBase + File.separator + PLAYER_PATH + File.separator + uuid + ".data")));
            BandePlayer data = (BandePlayer) in.readObject();
            in.close();
            return data;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Deprecated
    public boolean savePlayer(BandePlayer player) {
        try {
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(
                    new GZIPOutputStream(
                            new FileOutputStream(
                                    fileBase + File.separator + PLAYER_PATH + File.separator
                                            + player.getBase().getUniqueId() + ".data")));
            out.writeObject(player);
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Deprecated
    public BandeTop loadTop() {

        if (!topExists()) {
            BandeTop top = new BandeTop();

            saveTop(top);
            return top;
        }

        try {
            BukkitObjectInputStream in = new BukkitObjectInputStream(
                    new GZIPInputStream(
                            new FileInputStream(
                                    fileBase + File.separator + BANDE_TOP_PATH + File.separator + "top.data")));
            BandeTop data = (BandeTop) in.readObject();
            in.close();
            return data;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Deprecated
    public boolean saveTop(BandeTop top) {
        try {
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(
                    new GZIPOutputStream(
                            new FileOutputStream(
                                    fileBase + File.separator + BANDE_TOP_PATH + File.separator
                                            + "top" + ".data")));
            out.writeObject(top);
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Deprecated
    public dk.tandhjulet.huse.HouseHolder loadHouses() {

        if (!holderExists()) {
            dk.tandhjulet.huse.HouseHolder holder = new dk.tandhjulet.huse.HouseHolder();

            saveHouses(holder);
            return holder;
        }

        try {
            BukkitObjectInputStream in = new BukkitObjectInputStream(
                    new GZIPInputStream(
                            new FileInputStream(
                                    fileBase + File.separator + BANDE_HOUSE_HOLDER_PATH + File.separator
                                            + "houses.data")));
            dk.tandhjulet.huse.HouseHolder data = (dk.tandhjulet.huse.HouseHolder) in.readObject();
            in.close();
            return data;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Deprecated
    public boolean saveHouses(dk.tandhjulet.huse.HouseHolder holder) {
        try {
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(
                    new GZIPOutputStream(
                            new FileOutputStream(
                                    fileBase + File.separator + BANDE_HOUSE_HOLDER_PATH + File.separator
                                            + "houses.data")));
            out.writeObject(holder);
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Deprecated
    public InventoryData loadInvDataHolder() {

        if (!invDataHolderExists()) {
            InventoryData holder = new InventoryData();

            saveInvDataHolder(holder);
            return holder;
        }

        try {
            BukkitObjectInputStream in = new BukkitObjectInputStream(
                    new GZIPInputStream(
                            new FileInputStream(
                                    fileBase + File.separator + BANDE_TOP_PATH + File.separator
                                            + "inventorydata.data")));
            InventoryData data = (InventoryData) in.readObject();
            in.close();
            return data;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Deprecated
    public boolean saveInvDataHolder(InventoryData holder) {
        try {
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(
                    new GZIPOutputStream(
                            new FileOutputStream(
                                    fileBase + File.separator + BANDE_TOP_PATH + File.separator
                                            + "inventorydata.data")));
            out.writeObject(holder);
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Deprecated
    public boolean invDataHolderExists() {
        return new File(fileBase + File.separator + BANDE_TOP_PATH + File.separator + "inventorydata.data").exists();
    }

    @Deprecated
    public boolean holderExists() {
        return new File(fileBase + File.separator + BANDE_HOUSE_HOLDER_PATH + File.separator + "houses.data").exists();
    }

    @Deprecated
    public boolean topExists() {
        return new File(fileBase + File.separator + BANDE_TOP_PATH + File.separator + "top.data").exists();
    }

    @Deprecated
    public boolean guiExists(String gui) {
        return new File(fileBase + File.separator + GUI_PATH + File.separator + gui + ".data").exists();
    }

    @Deprecated
    public boolean bandeExists(String bande) {
        return new File(fileBase + File.separator + BANDE_PATH + File.separator + bande + ".data").exists();
    }

    @Deprecated
    public boolean playerExists(String uuid) {
        return new File(fileBase + File.separator + PLAYER_PATH + File.separator + uuid + ".data")
                .exists();
    }

    public static File getGUIFile(String gui) {
        return new File(fileBase + File.separator + GUI_PATH + File.separator + gui + ".yml");
    }

    public static File getBandeFile(String bande) {
        return new File(fileBase + File.separator + BANDE_PATH + File.separator + bande + ".yml");
    }

    public static File getUserFile(UUID uuid) {
        return new File(fileBase + File.separator + PLAYER_PATH + File.separator + uuid.toString() + ".yml");
    }

    public static File getTopFile() {
        return new File(fileBase + File.separator + BANDE_TOP_PATH + File.separator + "top.yml");
    }

    public static File getInventoryDataFile() {
        return new File(fileBase + File.separator + BANDE_TOP_PATH + File.separator + "inventorydata.yml");
    }
}
