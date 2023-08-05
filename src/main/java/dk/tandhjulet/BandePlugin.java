package dk.tandhjulet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import dk.tandhjulet.api.BandeAPI;
import dk.tandhjulet.api.HeadDataBaseAPI;
import dk.tandhjulet.api.nbt.NBTAPI;
import dk.tandhjulet.commands.CommandBAC;
import dk.tandhjulet.commands.CommandBande;
import dk.tandhjulet.commands.CommandBandeAdmin;
import dk.tandhjulet.commands.CommandItemBuilder;
import dk.tandhjulet.config.IConfig;
import dk.tandhjulet.gui.GUIManager;
import dk.tandhjulet.gui.InventoryData;
import dk.tandhjulet.gui.TypeManager;
import dk.tandhjulet.huse.HouseManager;
import dk.tandhjulet.level.LevelManager;
import dk.tandhjulet.listeners.ChatListener;
import dk.tandhjulet.listeners.DamageListener;
import dk.tandhjulet.listeners.DeathListener;
import dk.tandhjulet.listeners.JoinListener;
import dk.tandhjulet.placeholders.Placeholders;
import dk.tandhjulet.storage.Config;
import dk.tandhjulet.storage.FileManager;
import dk.tandhjulet.storage.Message;
import dk.tandhjulet.top.BandeTop;
import dk.tandhjulet.update.UpdateChecker;
import dk.tandhjulet.utils.Logger;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class BandePlugin extends JavaPlugin {
    private static FileManager fm;
    private static BandeAPI api;

    private static BandePlugin plugin;
    private static GUIManager guiManager;
    private static TypeManager typeManager;
    private static LevelManager levelManager;

    private static List<IConfig> configList = new ArrayList<>();

    @SuppressWarnings("deprecation")
    private static dk.tandhjulet.huse.HouseHolder houseHolder;

    private static HouseManager houseManager;
    private static Config config;

    private static SkriptAddon addon;

    private static Economy econ = null;
    private static Permission perms = null;
    private static Chat chat = null;

    private static boolean chatEnabled = false;

    private static InventoryData inventoryDataHolder;

    private static BandeTop top;

    public static boolean isPAPIEnabled;

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            Logger.severe("Lukker pluginnet ned - kunne ikke finde et economy-plugin.");
            Logger.severe("Installer EssentialsX eller et lign. plugin, for at løse dette problem.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (setupChat()) {
            Logger.severe("Lukker pluginnet ned - kunne ikke finde et chat-plugin.");
            Logger.severe("Installer EssentialsXChat eller et lign. plugin, for at løse dette problem.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (setupPermissions()) {
            Logger.severe("Lukker pluginnet ned - kunne ikke finde et permissions-plugin.");
            Logger.severe("Installer LuckPerms, GroupManager eller et lign. plugin, for at løse dette problem.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.saveDefaultConfig();
        plugin = this;

        // For backwards compatability
        loadDeprecatedVariables();

        config = new Config();

        final File messages = new File(this.getDataFolder(), "messages.yml");
        if (!messages.exists()) {
            this.saveResource("messages.yml", false);
        }
        Message.init();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Logger.info("Found Placeholder API! Registering expansion...");
            new Placeholders(this).register();
            isPAPIEnabled = true;
        } else {
            isPAPIEnabled = false;
            Logger.warn("PlaceholderAPI not installed/found!");
        }

        api = new BandeAPI();

        new HeadDataBaseAPI();
        NBTAPI.init();

        inventoryDataHolder = new InventoryData();

        typeManager = new TypeManager();
        guiManager = new GUIManager();
        guiManager.reload();

        top = new BandeTop();
        levelManager = new LevelManager();

        configList.add(config);
        configList.add(inventoryDataHolder);
        configList.add(top);

        PluginManager pm = Bukkit.getPluginManager();
        if (config.getPrefferedProvider().equalsIgnoreCase("auto")) {
            if (pm.getPlugin("Cells") != null && pm.getPlugin("Cells").isEnabled()) {
                houseManager = new HouseManager("Cells");
            } else if (pm.getPlugin("PrisonCells") != null && pm.getPlugin("PrisonCells").isEnabled()) {
                houseManager = new HouseManager("Cells");
            } else if (pm.getPlugin("AreaShop") != null && pm.getPlugin("AreaShop").isEnabled()) {
                houseManager = new HouseManager("areashop");
            } else {
                houseManager = new HouseManager(null);
            }
        } else {
            houseManager = new HouseManager(config.getPrefferedProvider());
        }

        this.getCommand("bande").setExecutor(new CommandBande());
        this.getCommand("itembuilder").setExecutor(new CommandItemBuilder());
        this.getCommand("bac").setExecutor(new CommandBAC());
        this.getCommand("bandeadmin").setExecutor(new CommandBandeAdmin());

        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new DamageListener(), this);

        if (pm.getPlugin("Skript") != null && pm.getPlugin("Skript").isEnabled()) {
            addon = Skript.registerAddon(this);

            try {
                addon.loadClasses("dk.tandhjulet", "skript");
                getLogger().info("Registered bande as Skript-addon!");
            } catch (IOException exception) {
                exception.printStackTrace();
                getLogger().info("Failed to register skript-syntaxes. (An error occured, refer to stacktrace)");
            }
        } else {
            getLogger().info("Failed to register skript-syntaxes. (Plugin not found/disabled)");
        }

        UpdateChecker.fetchLatestRelease();

        getLogger().info("Bande er blevet indlæst.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin shutting down...");
    }

    @SuppressWarnings("deprecation")
    public void loadDeprecatedVariables() {
        houseHolder = (dk.tandhjulet.huse.HouseHolder) FileManager.loadDeprecated(FileManager.getHouseHolderFile());
    }

    public static InventoryData getInventoryDataHolder() {
        return inventoryDataHolder;
    }

    public static void reload() {
        configList.forEach(i -> i.reloadConfig());
    }

    public static void setChatEnabled(boolean to) {
        chatEnabled = to;
    }

    public static boolean getChatEnabled() {
        return chatEnabled;
    }

    public static HouseManager getHouseManager() {
        return houseManager;
    }

    @Deprecated
    public static dk.tandhjulet.huse.HouseHolder getHouseHolder() {
        return houseHolder;
    }

    public static List<String> getHouses() {
        return getConfiguration().getHouses();
    }

    public static void setLevelManager(LevelManager levelManager) {
        BandePlugin.levelManager = levelManager;
    }

    public static LevelManager getLevelManager() {
        return levelManager;
    }

    public static Integer scheduleSyncRepeatingTask(Runnable runnable, Long timer) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(BandePlugin.getPlugin(), runnable, timer, timer);
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp == null) {
            return false;
        }
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null) {
            return false;
        }
        perms = rsp.getProvider();
        return perms != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static Permission getPermissions() {
        return perms;
    }

    public static Chat getChat() {
        return chat;
    }

    public static BandeTop getTop() {
        return top;
    }

    public static Config getConfiguration() {
        return config;
    }

    public static TypeManager getTypeManager() {
        return typeManager;
    }

    public static GUIManager getGuiManager() {
        return guiManager;
    }

    public static boolean isPAPIEnabled() {
        return isPAPIEnabled;
    }

    @Deprecated
    public static FileManager getFileManager() {
        return fm;
    }

    public static BandeAPI getAPI() {
        return api;
    }

    public static BandePlugin getPlugin() {
        return plugin;
    }

}