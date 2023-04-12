package dk.tandhjulet.events;

import java.math.BigDecimal;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import dk.tandhjulet.bande.Bande;

public class BandeBankUpdateEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Bande bande;
    private final BigDecimal amount;
    private final BigDecimal newBalance;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public BandeBankUpdateEvent(Bande bande, BigDecimal amount, BigDecimal newBalance) {
        this.bande = bande;
        this.amount = amount;
        this.newBalance = newBalance;
    }

    public Bande getBande() {
        return bande;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getNewBalance() {
        return newBalance;
    }
}
