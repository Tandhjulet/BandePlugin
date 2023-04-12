package dk.tandhjulet.hooks;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import dk.tandhjulet.utils.Logger;

public class WorldGuardHook {
    protected static WorldGuardPlugin getWorldGuard() {
        return (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
    }

    public static void addMember(String regionId, UUID uuid, World world) {
        RegionManager rManager = getWorldGuard().getRegionManager(world);
        if (rManager.hasRegion(regionId)) {
            ProtectedRegion region = rManager.getRegion(regionId);
            if (region == null) {
                return;
            }
            if (region.getMembers().contains(uuid)) {
                return;
            }

            region.getMembers().addPlayer(uuid);
            region.setDirty(true);

        } else {
            Logger.warn("Invalid region (" + regionId + ") for world " + world.getName() + " requested.");
        }
    }

    public static void removeMember(String regionId, UUID uuid, World world) {
        RegionManager rManager = getWorldGuard().getRegionManager(world);
        if (rManager.hasRegion(regionId)) {
            ProtectedRegion region = rManager.getRegion(regionId);
            if (region == null) {
                return;
            }

            region.setDirty(true);
            region.getMembers().removePlayer(uuid);

            try {
                rManager.saveChanges();
            } catch (StorageException e) {
                e.printStackTrace();
            }
        } else {
            Logger.warn("Invalid region (" + regionId + ") for world " + world.getName() + " requested.");
        }
    }
}
