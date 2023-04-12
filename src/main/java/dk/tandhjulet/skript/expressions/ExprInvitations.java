package dk.tandhjulet.skript.expressions;

import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.bande.BandePlayer;

@SuppressWarnings("all")
public class ExprInvitations extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprInvitations.class, String.class, ExpressionType.PROPERTY,
                "invitation[s] of %bandeplayer%", "%bandeplayer%['s] (invitation[s]|invite[s])");
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    Expression<BandePlayer> player;

    @Override
    public boolean init(Expression<?>[] arg0, int arg1, Kleenean arg2, ParseResult arg3) {
        player = (Expression<BandePlayer>) arg0[0];
        return true;
    }

    @Override
    public String toString(Event arg0, boolean arg1) {
        return "invitations of " + player.toString(arg0, arg1);
    }

    @Override
    protected String[] get(Event arg0) {
        if (player.getSingle(arg0) == null) {
            return null;
        }
        return player.getSingle(arg0).getInvitations().toArray(new String[0]);
    }

}