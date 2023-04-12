package dk.tandhjulet.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.bande.BandePlayer;

public class BandeDemoteEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final BandePlayer player;
    private final Bande bande;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public BandeDemoteEvent(BandePlayer player, Bande bande) {
        this.player = player;
        this.bande = bande;
    }

    public Bande getBande() {
        return bande;
    }

    public BandePlayer getDemoted() {
        return player;
    }
}