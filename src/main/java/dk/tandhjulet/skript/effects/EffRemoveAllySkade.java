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

@SuppressWarnings("all")
public class EffRemoveAllySkade extends Effect {
    static {
        Skript.registerEffect(EffRemoveAllySkade.class,
                "remove [1 [of]] ally[(-| )]skade [from] [bande] %bande%");
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
        return "remove 1 allyskade from bande " + bande.toString(event, debug);
    }

    @Override
    protected void execute(Event event) {
        bande.getSingle(event).removeAllySkade();
    }
}