package dk.tandhjulet.skript.effects;

import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.function.Function;
import ch.njol.skript.lang.function.Functions;
import ch.njol.util.Kleenean;
import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.placeholders.BandePlaceholders;

public class EffAddPlaceholder extends Effect {
    static {
        Skript.registerEffect(EffAddPlaceholder.class,
                "(add|register) placeholder %string% (with|by) [function ]%string%");
    }

    private Expression<String> typeName;
    private Expression<String> functionName;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        this.typeName = (Expression<String>) expressions[0];
        this.functionName = (Expression<String>) expressions[1];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "register type " + typeName.toString(event, debug) + " with function "
                + functionName.toString(event, debug);
    }

    @Override
    protected void execute(Event event) {
        Function<?> func = Functions.getFunction(functionName.getSingle(event));

        if (func.getParameters().length != 1) {
            Skript.warning(func.getName() + " doesn't have exactly 1 parameter");
        } else if (func.getParameter(0).getType().getC() != Bande.class) {
            Skript.warning(func.getName() + " 1st parameter isn't of type bande!");
        } else if (func.getReturnType().getC() != String.class) {
            Skript.warning(func.getName() + " return type is not a String!");
        }

        BandePlaceholders.registerPlaceholder(typeName.getSingle(event), bande -> ((String) func
                .execute(new Object[][] { { bande } })[0]));
    }
}
