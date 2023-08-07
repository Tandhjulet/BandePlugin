package dk.tandhjulet.skript.effects;

import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.bande.BandePlayer;

public class EffKickBandeMember extends Effect {
    static {
        Skript.registerEffect(EffKickBandeMember.class,
                "kick [bande] [member] %bandeplayer% from [bande] %bande% with sender %bandeplayer%");
    }

    private Expression<BandePlayer> toKick;
    private Expression<BandePlayer> sender;
    private Expression<Bande> kickFrom;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        this.toKick = (Expression<BandePlayer>) expressions[0];
        this.kickFrom = (Expression<Bande>) expressions[1];
        this.sender = (Expression<BandePlayer>) expressions[2];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "kick bande member " + toKick.toString(event, debug) + " from bande " + kickFrom.toString(event, debug)
                + " with sender " + sender.toString(event, debug);
    }

    @Override
    protected void execute(Event event) {
        kickFrom.getSingle(event).kick(sender.getSingle(event), toKick.getSingle(event).getBase().getUniqueId());
    }
}