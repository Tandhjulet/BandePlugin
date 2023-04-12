package dk.tandhjulet.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.bande.BandePlayer;

public class BandeKickEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final BandePlayer kicked;
    private final Bande bande;
    private final BandePlayer sender;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public BandeKickEvent(BandePlayer kicked, BandePlayer sender, Bande bande) {
        this.kicked = kicked;
        this.sender = sender;
        this.bande = bande;
    }

    public BandePlayer getKicked() {
        return kicked;
    }

    public BandePlayer getSender() {
        return sender;
    }

    public Bande getBande() {
        return bande;
    }
}