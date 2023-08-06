package dk.tandhjulet.api;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;

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
import dk.tandhjulet.huse.HouseManager;
import dk.tandhjulet.placeholders.BandePlaceholders;
import dk.tandhjulet.storage.FileManager;

public class BandeAPI {
    private LoadingCache<String, Bande> bandeCache;
    private LoadingCache<UUID, BandePlayer> playerCache;

    public BandeAPI() {
        CacheLoader<String, Bande> bandeLoader = new CacheLoader<String, Bande>() {

            @Override
            public Bande load(String bandeName) {
                return FileManager.getBande(bandeName);
            }
        };
        bandeCache = CacheBuilder.newBuilder().build(bandeLoader);

        CacheLoader<UUID, BandePlayer> playerLoader = new CacheLoader<UUID, BandePlayer>() {

            @Override
            public BandePlayer load(UUID uuid) {
                return FileManager.loadUncachedUser(uuid);
            }
        };
        playerCache = CacheBuilder.newBuilder().build(playerLoader);
    }

    public void addToCache(Player player, BandePlayer bandeplayer) {
        playerCache.put(player.getUniqueId(), bandeplayer);
    }

    public void addToCache(String bandeName, Bande bande) {
        bandeCache.put(bandeName, bande);
    }

    public synchronized BandePlayer getPlayer(UUID uuid) {
        return playerCache.getUnchecked(uuid);
    }

    public BandePlayer getIfPresent(Player player) {
        return playerCache.getIfPresent(player);
    }

    public Bande getIfPresent(String bande) {
        return bandeCache.getIfPresent(bande);
    }

    public synchronized BandePlayer getPlayer(Player player) {
        BandePlayer p = playerCache.getUnchecked(player.getUniqueId());
        if (p == null || p.isDestroyed()) {
            return null;
        }
        return p;
    }

    public synchronized Bande getBande(String bande) {
        Bande b = bandeCache.getIfPresent(bande);
        if (b == null || b.isDestroyed()) {
            return null;
        }
        return b;
    }

    public Bande createBande(String name, UUID owner) {
        Bande bande = new Bande(name, owner);
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

    @Deprecated
    public dk.tandhjulet.huse.HouseHolder getHouseHolder() {
        return BandePlugin.getHouseHolder();
    }

    public void addMember(String regionId, UUID uuid, World world) {
        WorldGuardHook.addMember(regionId, uuid, world);
    }

    public void removeMember(String regionId, UUID uuid, World world) {
        WorldGuardHook.removeMember(regionId, uuid, world);
    }
}
