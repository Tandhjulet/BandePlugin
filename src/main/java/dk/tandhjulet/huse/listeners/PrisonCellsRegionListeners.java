package dk.tandhjulet.huse.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import dk.plexhost.cells.events.cell.CellRentedEvent;
import dk.plexhost.cells.events.cell.CellUnrentedEvent;
import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.storage.Message;

public class PrisonCellsRegionListeners implements Listener {

    @EventHandler
    public void onCellRent(CellRentedEvent e) {

        if (!BandePlugin.getHouses().contains(e.getCell().getName())) {
            return;
        }

        BandePlayer player = BandePlugin.getAPI().getPlayer(e.getPlayer().getUniqueId());
        if (!player.hasBande()) {
            e.setCancelled(true);
            Message.sendReplaced(e.getPlayer(), "bande_huse.cells_denied_rent", null);
            return;
        }
        if (player.getBande().hasBandeHus()) {
            e.setCancelled(true);
            Message.sendReplaced(e.getPlayer(), "bande_huse.already_owns", null);
            return;
        }

        Integer reqLevel = BandePlugin.getConfiguration().getBandeHusReqLevel();
        if (reqLevel > player.getBande().getLevel()) {
            e.setCancelled(true);
            Message.sendReplaced(e.getPlayer(), "bande_huse.too_low_level", null, reqLevel.toString());
            return;
        }

        player.getBande().setBandeHus(e.getCell().getName());
        Message.sendReplaced(player.getBande().getMemberIterable(), "bande_huse.bought", null, e.getPlayer().getName(),
                e.getCell().getName());
    }

    @EventHandler
    public void onCellUnrent(CellUnrentedEvent e) {
        if (!BandePlugin.getHouses().contains(e.getCell().getName())) {
            return;
        }

        BandePlayer player = BandePlugin.getAPI().getPlayer(e.getOwner());
        if (!player.hasBande()) {
            return;
        }

        player.getBande().setBandeHus(null);
    }
}
