package dk.tandhjulet.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import dk.tandhjulet.bande.Bande;

public class BandeNewAccessEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final String region;
    private final Bande bande;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public BandeNewAccessEvent(Bande bande, String regionId) {
        this.region = regionId;
        this.bande = bande;
    }

    public String getRegionId() {
        return region;
    }

    public Bande getBande() {
        return bande;
    }

}
