package dk.tandhjulet.skript.expressions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.event.Event;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.Bande;

@SuppressWarnings("all")
public class ExprBande extends SimpleExpression<Bande> {
    static {
        Skript.registerExpression(ExprBande.class, Bande.class, ExpressionType.PROPERTY,
                "bande [with [the]] name[d] %string%");
    }

    private Expression<String> name;

    @Override
    public @Nonnull Class<? extends Bande> getReturnType() {
        return Bande.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(@Nonnull Expression<?>[] arg0, int arg1, @Nonnull Kleenean arg2, @Nonnull ParseResult arg3) {
        this.name = (Expression<String>) arg0[0];
        return true;
    }

    @Override
    public @Nonnull String toString(@Nullable Event arg0, boolean arg1) {
        return "bande named " + name.toString(arg0, arg1);
    }

    @Override
    @Nullable
    protected Bande[] get(Event arg0) {
        return new Bande[] { BandePlugin.getAPI().getBande(name.getSingle(arg0)) };
    }
}
