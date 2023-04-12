package dk.tandhjulet.skript.expressions;

import java.util.HashSet;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableList;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.enums.BandeRank;

@SuppressWarnings("all")
public class ExprBandeMembers extends SimpleExpression<UUID> {

    static {
        Skript.registerExpression(ExprBandeMembers.class, UUID.class, ExpressionType.COMBINED,
                "[bande ]members [of ]%bande% (with|having) [rank] %banderank%",
                "[all ][bande ]members [of ]%bande%");
    }

    private Expression<Bande> bande;
    private Expression<BandeRank> rank;

    private int pattern;

    @Override
    public Class<? extends UUID> getReturnType() {
        return UUID.class;
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(@Nonnull Expression<?>[] arg0, int arg1, @Nonnull Kleenean arg2, @Nonnull ParseResult arg3) {
        this.pattern = arg1;

        this.bande = (Expression<Bande>) arg0[0];
        if (arg1 == 0)
            this.rank = (Expression<BandeRank>) arg0[1];
        return true;
    }

    @Override
    public String toString(@Nullable Event arg0, boolean arg1) {
        if (pattern == 1) {
            return "bande members of " + bande.toString(arg0, arg1);
        }
        return "bande members of " + bande.toString(arg0, arg1) + " with rank " + rank.toString(arg0, arg1);
    }

    @Override
    @Nullable
    protected UUID[] get(@Nonnull Event arg0) {
        if (bande.getSingle(arg0) == null)
            return null;

        if (pattern == 1) {
            Iterable<UUID> members = bande.getSingle(arg0).getMemberIterable();

            return ImmutableList.copyOf(members).toArray(new UUID[0]);
        } else if (pattern == 0) {
            HashSet<UUID> members = bande.getSingle(arg0).getMembers().get(rank.getSingle(arg0));
            return members.toArray(new UUID[0]);
        }
        return null;
    }
}
