package dk.tandhjulet.skript.expressions;

import javax.annotation.Nullable;

import org.bukkit.event.Event;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import dk.tandhjulet.bande.Bande;

@SuppressWarnings("all")
public class ExprBandeAccess extends SimpleExpression<String> {
    static {
        Skript.registerExpression(ExprBandeAccess.class, String.class, ExpressionType.PROPERTY,
                "%bande%['s] access[ list]", "access[[(-| )]list] [of ]%bande%");
    }

    Expression<Bande> bande;

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
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
        return "access list of " + bande.toString(arg0, arg1);
    }

    @Override
    @Nullable
    protected String[] get(Event arg0) {
        return bande.getSingle(arg0).getAccessList().toArray(new String[0]);
    }
}
