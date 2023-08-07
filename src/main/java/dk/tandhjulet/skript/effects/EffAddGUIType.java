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
import dk.tandhjulet.BandePlugin;

public class EffAddGUIType extends Effect {
    static {
        Skript.registerEffect(EffAddGUIType.class, "(add|register) type %string% (with|by) [function ]%string%");
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

        if (func.getParameters().length == 0) {
            BandePlugin.getTypeManager().addType(typeName.getSingle(event), e -> {
                func.execute(new Object[][] {});
            });
        } else {
            BandePlugin.getTypeManager().addType(typeName.getSingle(event), e -> {
                func.execute(new Object[][] { { e } });
            });
        }
    }
}
