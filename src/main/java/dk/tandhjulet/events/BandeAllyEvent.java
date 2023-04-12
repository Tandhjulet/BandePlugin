package dk.tandhjulet.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import dk.tandhjulet.bande.Bande;

public class BandeAllyEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Bande to;
    private final Bande from;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public BandeAllyEvent(Bande to, Bande from) {
        this.to = to;
        this.from = from;
    }

    public Bande getTo() {
        return to;
    }

    public Bande getFrom() {
        return from;
    }
}