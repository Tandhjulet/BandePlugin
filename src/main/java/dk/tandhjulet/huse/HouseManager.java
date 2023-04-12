package dk.tandhjulet.huse;

import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Player;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.huse.listeners.AreaShopRegionListeners;
import dk.tandhjulet.huse.listeners.PrisonCellsRegionListeners;
import dk.tandhjulet.huse.providers.AreaShopAPI;
import dk.tandhjulet.huse.providers.IProvider;
import dk.tandhjulet.huse.providers.PrisonCellsAPI;
import dk.tandhjulet.utils.Logger;

public class HouseManager {
    public Boolean enabled;
    public IProvider provider;

    public HouseManager(String provider) {
        if (provider == null) {
            Logger.warn("Fandt intet celle-plugin. (Kun AreaShop og Plexhost's PrisonCells understøttes)");
            Logger.warn("Slår bande-huse fra...");
            enabled = false;
            return;
        }

        if (provider.equalsIgnoreCase("cells")) {
            this.provider = new PrisonCellsAPI();
            Logger.info("Bruger Cells til bande huse...");

            BandePlugin.getPlugin().getServer().getPluginManager().registerEvents(new PrisonCellsRegionListeners(),
                    BandePlugin.getPlugin());
            enabled = true;
        } else if (provider.equalsIgnoreCase("areashop")) {
            this.provider = new AreaShopAPI();
            Logger.info("Bruger AreaShop til bande huse...");
            BandePlugin.getPlugin().getServer().getPluginManager().registerEvents(new AreaShopRegionListeners(),
                    BandePlugin.getPlugin());
            enabled = true;
        } else {
            Logger.warn("Fandt intet celle-plugin. (Kun AreaShop og Plexhost's PrisonCells understøttes)");
            Logger.warn("Slår bande-huse fra...");
            enabled = false;
        }
    }

    public List<String> getAvailableRegions(World world) {
        if (!enabled)
            return null;
        return provider.getAvailableHouses(world);
    }

    public void rent(Player player, String cellName) {
        provider.makePlayerRent(player, cellName);
    }

    public List<String> getRegions() {
        if (!enabled)
            return null;
        return provider.getAllHouses();
    }

    public Boolean exists(String cellName) {
        if (!enabled)
            return null;
        return provider.exists(cellName);
    }

    public Integer getPrice(String regionName) {
        if (!enabled)
            return null;
        return provider.getPrice(regionName);
    }

    public Boolean isEnabled() {
        return enabled;
    }
}
