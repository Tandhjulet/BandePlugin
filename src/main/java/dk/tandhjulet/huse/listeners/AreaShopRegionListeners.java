package dk.tandhjulet.huse.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.storage.Message;
import me.wiefferink.areashop.events.ask.RentingRegionEvent;
import me.wiefferink.areashop.events.ask.UnrentingRegionEvent;

public class AreaShopRegionListeners implements Listener {

    @EventHandler
    public void onRegionRent(RentingRegionEvent e) {
        if (!BandePlugin.getHouses().contains(e.getRegion().getName())) {
            return;
        }

        BandePlayer player = BandePlugin.getAPI().getPlayer(e.getPlayer().getUniqueId());
        if (!player.hasBande()) {
            e.cancel(String.join(" ", Message.get("bande_huse.areashop_denied_rent")));
            return;
        }
        if (player.getBande().hasBandeHus()) {
            e.cancel(String.join(" ", Message.get("bande_huse.already_owns")));
            return;
        }
        Integer reqLevel = BandePlugin.getConfiguration().getBandeHusReqLevel();
        if (reqLevel > player.getBande().getLevel()) {
            e.cancel(Message.getReplaced("bande_huse.too_low_level", reqLevel.toString()).get(0).toString());
            return;
        }

        player.getBande().setBandeHus(e.getRegion().getName());
        Message.sendReplaced(player.getBande().getMemberIterable(), "bande_huse.bought", null, e.getPlayer().getName(),
                e.getRegion().getName());
    }

    @EventHandler
    public void onRegionUnrent(UnrentingRegionEvent e) {
        if (!BandePlugin.getHouses().contains(e.getRegion().getName())) {
            return;
        }

        BandePlayer player = BandePlugin.getAPI().getPlayer(e.getRegion().getOwner());
        if (!player.hasBande()) {
            return;
        }

        player.getBande().setBalance(null);
    }
}
