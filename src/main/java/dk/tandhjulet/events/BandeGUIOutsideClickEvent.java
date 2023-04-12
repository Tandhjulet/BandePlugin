package dk.tandhjulet.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;

public class BandeGUIOutsideClickEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final InventoryClickEvent event;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public BandeGUIOutsideClickEvent(InventoryClickEvent e) {
        this.event = e;
    }

    public InventoryClickEvent getEvent() {
        return event;
    }
}