package dk.tandhjulet.skript.expressions;

import java.math.BigDecimal;

import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.Bande;

@SuppressWarnings("all")
public class ExprBandeBalance extends SimplePropertyExpression<Bande, Number> {

    static {
        register(ExprBandeBalance.class, Number.class, "bank[ balance]", "bande");
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    @Nullable
    public Number convert(Bande bande) {
        return bande.getBalance();
    }

    @Override
    protected String getPropertyName() {
        return "bank[ balance]";
    }

    @Override
    public void change(Event event, Object[] delta, ChangeMode mode) {
        if (delta != null) {
            Bande bande = getExpr().getSingle(event);
            long amount = ((long) delta[0]);

            switch (mode) {
                case ADD:
                    bande.addToBalance(new BigDecimal(amount));
                    break;
                case REMOVE:
                    bande.removeBalance(new BigDecimal(amount));
                    break;
                case DELETE:
                    bande.setBalance(new BigDecimal(0));
                    break;
                case REMOVE_ALL:
                    bande.setBalance(new BigDecimal(0));
                    break;
                case RESET:
                    bande.setBalance(new BigDecimal(0));
                    break;
                case SET:
                    bande.setBalance(new BigDecimal(amount));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public Class<?>[] acceptChange(final ChangeMode mode) {
        return CollectionUtils.array(Number.class);
    }
}