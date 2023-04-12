package dk.tandhjulet.huse.providers;

import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Player;

public interface IProvider {

    public List<String> getAvailableHouses(World world);

    public List<String> getAllHouses();

    public Boolean exists(String cellName);

    public void makePlayerRent(Player p, String cellName);

    public Integer getPrice(String regionName);

}
