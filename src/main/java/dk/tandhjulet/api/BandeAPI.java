package dk.tandhjulet.api;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import dev.triumphteam.gui.components.GuiAction;
import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.gui.GUI;
import dk.tandhjulet.hooks.WorldGuardHook;
import dk.tandhjulet.huse.HouseHolder;
import dk.tandhjulet.huse.HouseManager;
import dk.tandhjulet.placeholders.BandePlaceholders;

public class BandeAPI {
    private LoadingCache<String, Optional<Bande>> bandeCache;
    private LoadingCache<UUID, BandePlayer> playerCache;

    public BandeAPI() {
        CacheLoader<String, Optional<Bande>> bandeLoader = new CacheLoader<String, Optional<Bande>>() {

            @Override
            public Optional<Bande> load(String bandeName) {
                Optional<Bande> result = Optional.ofNullable(BandePlugin.getFileManager().loadBande(bandeName));
                return result;
            }
        };
        bandeCache = CacheBuilder.newBuilder().build(bandeLoader);

        CacheLoader<UUID, BandePlayer> playerLoader = new CacheLoader<UUID, BandePlayer>() {

            @Override
            public BandePlayer load(UUID uuid) {
                return BandePlugin.getFileManager().loadPlayer(uuid);
            }
        };
        playerCache = CacheBuilder.newBuilder().build(playerLoader);
    }

    public synchronized void saveAllBander() {
        bandeCache.asMap().values().forEach(optional -> {
            Bande bande = optional.orElse(null);
            if (bande != null && bande.isDirty())
                bande.forceSave();
        });
    }

    public synchronized void saveAllPlayers() {
        playerCache.asMap().values().forEach(player -> {
            if (player.isDirty() && Bukkit.getOfflinePlayer(player.getBase().getUniqueId()).isOnline()
                    && Bukkit.getPlayer(player.getBase().getName()) != null)
                player.forceSave();
        });
    }

    public void addToCache(Player player, BandePlayer bandeplayer) {
        playerCache.put(player.getUniqueId(), bandeplayer);
    }

    public void addToCache(String bandeName, Bande bande) {
        bandeCache.put(bandeName, Optional.of(bande));
    }

    public synchronized BandePlayer getPlayer(UUID uuid) {
        return playerCache.getUnchecked(uuid);
    }

    public synchronized BandePlayer getPlayer(Player player) {
        return playerCache.getUnchecked(player.getUniqueId());
    }

    public synchronized Bande getBande(String bande) {
        return bandeCache.getUnchecked(bande).orElse(null);
    }

    public Bande createBande(String name, UUID owner) {
        Bande bande = new Bande(name, owner);
        bande.init();
        BandePlugin.getFileManager().saveBande(name, bande);

        return bande;
    }

    public void openGUI(final Player player, final String id) {
        this.openGUI(player, id, false, true);
    }

    public void openGUI(final Player player, final String id, final boolean interaction, final boolean handle) {
        BandePlugin.getGuiManager().openGui(player, id, interaction, handle);
    }

    public GUI getGUI(String id) {
        return BandePlugin.getGuiManager().getGUI(id);
    }

    public HashMap<String, GUI> getGUIs() {
        return BandePlugin.getGuiManager().getGUIs();
    }

    public void discardCache(final Bande bande) {
        bandeCache.invalidate(bande.getName());
    }

    public void discardCache(final BandePlayer player) {
        playerCache.invalidate(player.getBase().getUniqueId());
    }

    public GuiAction<InventoryClickEvent> getTypeAction(String type) {
        return BandePlugin.getTypeManager().get(type);
    }

    public void addType(String type, GuiAction<InventoryClickEvent> e) {
        BandePlugin.getTypeManager().addType(type, e);
    }

    public void registerBandePlaceholder(String identifier, Function<Bande, String> func) {
        BandePlaceholders.registerPlaceholder(identifier, func);
    }

    public Integer getFangeKills(String bandeName) {
        return BandePlugin.getTop().getFangeKill(bandeName);
    }

    public Integer getVagtKills(String bandeName) {
        return BandePlugin.getTop().getVagtKills(bandeName);
    }

    public Integer getOffiKills(String bandeName) {
        return BandePlugin.getTop().getOffiKills(bandeName);
    }

    public void removeBandeFromTop(String bandeName) {
        BandePlugin.getTop().removeBande(bandeName);
    }

    public HouseManager getHouseManager() {
        return BandePlugin.getHouseManager();
    }

    public HouseHolder getHouseHolder() {
        return BandePlugin.getHouseHolder();
    }

    public void addMember(String regionId, UUID uuid, World world) {
        WorldGuardHook.addMember(regionId, uuid, world);
    }

    public void removeMember(String regionId, UUID uuid, World world) {
        WorldGuardHook.removeMember(regionId, uuid, world);
    }
}
