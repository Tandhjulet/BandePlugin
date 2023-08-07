package dk.tandhjulet.skript.expressions;

import org.bukkit.event.Event;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import dk.tandhjulet.bande.Bande;

public class ExprRivals extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprRivals.class, String.class, ExpressionType.PROPERTY,
                "rival[s] of %bande%", "%bande%['s] rival[s]");
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    Expression<Bande> bande;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] arg0, int arg1, Kleenean arg2, ParseResult arg3) {
        bande = (Expression<Bande>) arg0[0];
        return true;
    }

    @Override
    public String toString(Event arg0, boolean arg1) {
        return "rivals of " + bande.toString(arg0, arg1);
    }

    @Override
    protected String[] get(Event arg0) {
        return bande.getSingle(arg0).getRivals().toArray(new String[0]);
    }

}