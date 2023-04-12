package dk.tandhjulet.skript.expressions;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.Bande;

@SuppressWarnings("all")
public class ExprPlayersBande extends SimpleExpression<Bande> {

    static {
        Skript.registerExpression(ExprPlayersBande.class, Bande.class, ExpressionType.PROPERTY, "%player%['s] bande",
                "bande of %player%");
    }

    private Expression<Player> player;

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
    public boolean init(Expression<?>[] arg0, int arg1, Kleenean arg2, ParseResult arg3) {
        this.player = (Expression<Player>) arg0[0];
        return true;
    }

    @Override
    public String toString(Event arg0, boolean arg1) {
        return "bande of " + player.toString(arg0, arg1);
    }

    @Override
    @Nullable
    protected Bande[] get(Event arg0) {
        return new Bande[] { BandePlugin.getAPI().getPlayer(player.getSingle(arg0)).getBande() };
    }

}
