package dk.tandhjulet.skript.expressions;

import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import dk.tandhjulet.bande.Bande;

@SuppressWarnings("all")
public class ExprBandeLevel extends SimpleExpression<Number> {

    static {
        Skript.registerExpression(ExprBandeLevel.class, Number.class, ExpressionType.PROPERTY, "%bande%['s] level",
                "level of %bande%");
    }

    private Expression<Bande> bande;

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] arg0, int arg1, Kleenean arg2, ParseResult arg3) {
        this.bande = (Expression<Bande>) arg0[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event arg0, boolean arg1) {
        return "level of " + bande.toString(arg0, arg1);
    }

    @Override
    @Nullable
    protected Number[] get(Event arg0) {
        return new Number[] { bande.getSingle(arg0).getLevel() };
    }
}
