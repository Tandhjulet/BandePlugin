package dk.tandhjulet.skript.effects;

import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import dk.tandhjulet.bande.Bande;

public class EffRemoveBandeskade extends Effect {
    static {
        Skript.registerEffect(EffRemoveBandeskade.class,
                "remove [1 [of]] bande[(-| )]skade [from] [bande] %bande%");
    }
    private Expression<Bande> bande;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        this.bande = (Expression<Bande>) expressions[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "remove 1 bandeskade from bande " + bande.toString(event, debug);
    }

    @Override
    protected void execute(Event event) {
        bande.getSingle(event).removeBandeSkade();
    }
}