package dk.tandhjulet.skript.effects;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.bande.BandePlayer;

public class EffInvitePlayer extends Effect {
    static {
        Skript.registerEffect(EffInvitePlayer.class,
                "invite [player] %player% to [bande] %bande% with [sender] %bandeplayer%");
    }

    private Expression<Player> toInvite;
    private Expression<BandePlayer> sender;
    private Expression<Bande> bande;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        this.toInvite = (Expression<Player>) expressions[0];
        this.sender = (Expression<BandePlayer>) expressions[2];
        this.bande = (Expression<Bande>) expressions[1];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "invite player " + toInvite.toString(event, debug) + " to bande " + bande.toString(event, debug)
                + " with sender " + sender.getSingle(event);
    }

    @Override
    protected void execute(Event event) {
        bande.getSingle(event).invite(sender.getSingle(event).getBase(), toInvite.getSingle(event).getUniqueId());
    }
}