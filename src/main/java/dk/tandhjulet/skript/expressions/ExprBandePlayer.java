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
import dk.tandhjulet.bande.BandePlayer;

public class ExprBandePlayer extends SimpleExpression<BandePlayer> {
    static {
        Skript.registerExpression(ExprBandePlayer.class, BandePlayer.class, ExpressionType.PROPERTY,
                "bande[( |-)]player [(with|from) [the] [player [instance]]] %player%");
    }

    private Expression<Player> player;

    @Override
    public Class<? extends BandePlayer> getReturnType() {
        return BandePlayer.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(@Nonnull Expression<?>[] arg0, int arg1, @Nonnull Kleenean arg2, @Nonnull ParseResult arg3) {
        this.player = (Expression<Player>) arg0[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event arg0, boolean arg1) {
        return "bande player from the player instance " + player.toString(arg0, arg1);
    }

    @Override
    @Nullable
    protected BandePlayer[] get(Event arg0) {
        return new BandePlayer[] { BandePlugin.getAPI().getPlayer(player.getSingle(arg0)) };
    }
}
