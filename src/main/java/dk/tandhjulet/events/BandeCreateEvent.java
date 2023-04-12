package dk.tandhjulet.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import dk.tandhjulet.bande.Bande;

public class BandeCreateEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Bande bande;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public BandeCreateEvent(Player player, Bande bande) {
        this.player = player;
        this.bande = bande;
    }

    public Player getPlayer() {
        return player;
    }

    public Bande getBande() {
        return bande;
    }
}
