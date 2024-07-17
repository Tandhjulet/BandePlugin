package dk.tandhjulet.huse;

import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Player;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.enums.Provider;
import dk.tandhjulet.huse.listeners.AreaShopRegionListeners;
import dk.tandhjulet.huse.listeners.PrisonCellsRegionListeners;
import dk.tandhjulet.huse.providers.AreaShopAPI;
import dk.tandhjulet.huse.providers.IProvider;
import dk.tandhjulet.huse.providers.PrisonCellsAPI;
import dk.tandhjulet.utils.Logger;

public class HouseManager {
    public Boolean enabled;
    public IProvider provider;

    public HouseManager(Provider provider) {
        if (provider == Provider.UNKNOWN) {
            Logger.warn("Fandt intet celle-plugin. (Kun AreaShop og Plexhost's PrisonCells understøttes)");
            Logger.warn("Slår bande-huse fra...");
            enabled = false;
            return;
        }

        else if (provider == Provider.CUSTOM) {
            Logger.info("Provider sat til custom. Afventer setProvider, og deaktiverer imellemtiden bandehuse.");
            enabled = false;
            return;
        }

        else if (provider == Provider.PRISON_CELLS) {
            this.provider = new PrisonCellsAPI();
            Logger.info("Bruger Cells til bande huse...");

            BandePlugin.getPlugin().getServer().getPluginManager().registerEvents(new PrisonCellsRegionListeners(),
                    BandePlugin.getPlugin());
            enabled = true;

        } else if (provider == Provider.AREASHOP) {
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

    public void setProvider(IProvider provider) {
        Logger.info("Provider sat. Genaktiverer bande huse.");
        this.provider = provider;
        this.enabled = true;
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
