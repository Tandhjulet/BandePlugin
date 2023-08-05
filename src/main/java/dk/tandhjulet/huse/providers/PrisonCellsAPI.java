package dk.tandhjulet.huse.providers;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Player;

import dk.plexhost.cells.cell.Cell;
import dk.plexhost.cells.managers.CellManager;
import dk.tandhjulet.BandePlugin;

public class PrisonCellsAPI implements IProvider {

    @Override
    public List<String> getAvailableHouses(World world) {
        List<String> out = new LinkedList<>();

        for (String cell : BandePlugin.getHouses()) {
            Cell region = CellManager.get().getCell(cell);
            if (region != null && !region.hasOwner()) {
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
        return CellManager.get().getCell(cellName) != null;
    }

    @Override
    public void makePlayerRent(Player p, String cellName) {
        CellManager.get().getCell(cellName).rent(p);
    }

    @Override
    public Integer getPrice(String cellName) {
        return (int) CellManager.get().getCell(cellName).getPrice();
    }

}
