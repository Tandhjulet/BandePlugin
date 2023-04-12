package dk.tandhjulet.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class BandeGUICloseEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final InventoryCloseEvent event;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public BandeGUICloseEvent(InventoryCloseEvent e) {
        this.event = e;
    }

    public InventoryCloseEvent getEvent() {
        return event;
    }
}