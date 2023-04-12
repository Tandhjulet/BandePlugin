package dk.tandhjulet.skript.expressions;

import javax.annotation.Nonnull;

import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;
import dk.tandhjulet.events.BandeBankUpdateEvent;
import dk.tandhjulet.events.BandeDepositEvent;

@SuppressWarnings("all")
public class ExprNewBalance extends SimpleExpression<Number> {
    static {
        Skript.registerExpression(ExprNewBalance.class, Number.class, ExpressionType.SIMPLE, "new balance");
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public boolean init(@Nonnull Expression<?>[] arg0, int arg1, @Nonnull Kleenean arg2, @Nonnull ParseResult arg3) {
        if (!ScriptLoader.isCurrentEvent(BandeBankUpdateEvent.class, BandeDepositEvent.class)) {
            Skript.error("Cannot use 'new balance' outside of a BandeBankUpdateEvent/BandeDepositEvent event",
                    ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        return true;
    }

    @Override
    public String toString(@Nullable Event arg0, boolean arg1) {
        return "new balance";
    }

    @Override
    @Nullable
    protected Number[] get(Event arg0) {
        return new Number[] { getNewBalance(arg0) };
    }

    private static Number getNewBalance(@Nullable Event e) {
        if (e == null)
            return null;
        if (e instanceof BandeBankUpdateEvent) {
            BandeBankUpdateEvent event = (BandeBankUpdateEvent) e;
            return event.getNewBalance();
        } else if (e instanceof BandeDepositEvent) {
            BandeDepositEvent event = (BandeDepositEvent) e;
            return event.getBande().getBalance();
        }
        return null;
    }
}
