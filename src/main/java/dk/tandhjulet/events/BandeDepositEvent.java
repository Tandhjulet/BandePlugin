package dk.tandhjulet.events;

import java.math.BigDecimal;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import dk.tandhjulet.bande.Bande;

public class BandeDepositEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Bande bande;
    private final BigDecimal amount;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public BandeDepositEvent(Player player, Bande bande, BigDecimal amount) {
        this.player = player;
        this.bande = bande;
        this.amount = amount;
    }

    public Player getPlayer() {
        return player;
    }

    public Bande getBande() {
        return bande;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
