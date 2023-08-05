package dk.tandhjulet.huse.providers;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import dk.tandhjulet.BandePlugin;
import me.wiefferink.areashop.AreaShop;
import me.wiefferink.areashop.managers.FileManager;
import me.wiefferink.areashop.regions.GeneralRegion;

public class AreaShopAPI implements IProvider {

    private static FileManager manager;

    public AreaShopAPI() {
        AreaShop as = (AreaShop) Bukkit.getPluginManager().getPlugin("AreaShop");
        if (as == null) {
            BandePlugin.getHouseManager().enabled = false;
            return;
        }

        manager = as.getFileManager();
    }

    @Override
    public List<String> getAvailableHouses(World world) {
        List<String> out = new LinkedList<>();

        for (String cell : BandePlugin.getHouses()) {
            GeneralRegion region = manager.getRegion(cell);
            if (region != null && region.isAvailable()) {
                out.add(cell);
            }
        }
        return out;
    }

    @Override
    public List<String> getAllHouses() {
        return BandePlugin.getHouses();
    }

    @Override
    public Boolean exists(String cellName) {
        return manager.getRegion(cellName) != null;
    }

    @Override
    public void makePlayerRent(Player p, String cellName) {
        manager.getRent(cellName).rent(p);
    }

    @Override
    public Integer getPrice(String cellName) {
        return (int) manager.getRent(cellName).getPrice();
    }
}