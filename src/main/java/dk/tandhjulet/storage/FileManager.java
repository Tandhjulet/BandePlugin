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
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.gui.GUI;
import dk.tandhjulet.gui.InvDataHolder;
import dk.tandhjulet.huse.HouseHolder;
import dk.tandhjulet.top.BandeTopHolder;

public class FileManager {
    private static String BANDE_PATH = "bander";
    private static String GUI_PATH = "guis";
    private static String PLAYER_PATH = "players";
    private static String BANDE_TOP_PATH = "data";
    private static String BANDE_HOUSE_HOLDER_PATH = "houses";

    private String fileBase;

    public FileManager(String fileBase) {
        this.fileBase = fileBase;

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
            data.init();
            in.close();
            return data;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

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

    public void removeBande(String bandeName) {
        File f = new File(fileBase + File.separator + BANDE_PATH + File.separator + bandeName + ".data");
        f.delete();
    }

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

    public BandePlayer loadPlayer(UUID uuid) {

        if (!playerExists(uuid.toString())) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

            if (player == null || !player.isOnline() || player.getPlayer() == null) {
                return null;
            }

            BandePlayer bandePlayer = new BandePlayer(player.getPlayer());
            bandePlayer.init();
            savePlayer(bandePlayer);
            return bandePlayer;
        }

        try {
            BukkitObjectInputStream in = new BukkitObjectInputStream(
                    new GZIPInputStream(
                            new FileInputStream(
                                    fileBase + File.separator + PLAYER_PATH + File.separator + uuid + ".data")));
            BandePlayer data = (BandePlayer) in.readObject();
            data.init();
            in.close();
            return data;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

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

    public BandeTopHolder loadTop() {

        if (!topExists()) {
            BandeTopHolder top = new BandeTopHolder();

            saveTop(top);
            return top;
        }

        try {
            BukkitObjectInputStream in = new BukkitObjectInputStream(
                    new GZIPInputStream(
                            new FileInputStream(
                                    fileBase + File.separator + BANDE_TOP_PATH + File.separator + "top.data")));
            BandeTopHolder data = (BandeTopHolder) in.readObject();
            in.close();
            return data;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean saveTop(BandeTopHolder top) {
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

    public HouseHolder loadHouses() {

        if (!holderExists()) {
            HouseHolder holder = new HouseHolder();

            saveHouses(holder);
            return holder;
        }

        try {
            BukkitObjectInputStream in = new BukkitObjectInputStream(
                    new GZIPInputStream(
                            new FileInputStream(
                                    fileBase + File.separator + BANDE_HOUSE_HOLDER_PATH + File.separator
                                            + "houses.data")));
            HouseHolder data = (HouseHolder) in.readObject();
            in.close();
            return data;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean saveHouses(HouseHolder holder) {
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

    public InvDataHolder loadInvDataHolder() {

        if (!invDataHolderExists()) {
            InvDataHolder holder = new InvDataHolder();

            saveInvDataHolder(holder);
            return holder;
        }

        try {
            BukkitObjectInputStream in = new BukkitObjectInputStream(
                    new GZIPInputStream(
                            new FileInputStream(
                                    fileBase + File.separator + BANDE_TOP_PATH + File.separator
                                            + "inventorydata.data")));
            InvDataHolder data = (InvDataHolder) in.readObject();
            in.close();
            return data;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean saveInvDataHolder(InvDataHolder holder) {
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

    public boolean invDataHolderExists() {
        return new File(fileBase + File.separator + BANDE_TOP_PATH + File.separator + "inventorydata.data").exists();
    }

    public boolean holderExists() {
        return new File(fileBase + File.separator + BANDE_HOUSE_HOLDER_PATH + File.separator + "houses.data").exists();
    }

    public boolean topExists() {
        return new File(fileBase + File.separator + BANDE_TOP_PATH + File.separator + "top.data").exists();
    }

    public boolean guiExists(String gui) {
        return new File(fileBase + File.separator + GUI_PATH + File.separator + gui + ".data").exists();
    }

    public boolean bandeExists(String bande) {
        return new File(fileBase + File.separator + BANDE_PATH + File.separator + bande + ".data").exists();
    }

    public boolean playerExists(String uuid) {
        return new File(fileBase + File.separator + PLAYER_PATH + File.separator + uuid + ".data")
                .exists();
    }
}
