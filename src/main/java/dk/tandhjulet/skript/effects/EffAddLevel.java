package dk.tandhjulet.skript.effects;

import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import dk.tandhjulet.bande.Bande;

public class EffAddLevel extends Effect {
    static {
        Skript.registerEffect(EffInvitePlayer.class,
                "add level [to] [bande] %bande%", "level[(-| )]up [the] [bande] %bande%");
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
        return "add level to bande " + bande.toString(event, debug);
    }

    @Override
    protected void execute(Event event) {
        bande.getSingle(event).addLevel();
    }
}