package dk.tandhjulet.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import dk.tandhjulet.bande.Bande;

public class BandeRivalEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Bande aggressor;
    private final Bande rival;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public BandeRivalEvent(Bande aggressor, Bande rival) {
        this.aggressor = aggressor;
        this.rival = rival;
    }

    public Bande getAggressor() {
        return aggressor;
    }

    public Bande getRival() {
        return rival;
    }
}