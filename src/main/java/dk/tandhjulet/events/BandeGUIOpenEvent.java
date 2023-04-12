package dk.tandhjulet.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class BandeGUIOpenEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final InventoryOpenEvent event;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public BandeGUIOpenEvent(InventoryOpenEvent e) {
        this.event = e;
    }

    public InventoryOpenEvent getEvent() {
        return event;
    }
}