package dk.tandhjulet.skript.expressions;

import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import dk.tandhjulet.bande.Bande;

@SuppressWarnings("all")
public class ExprBandeSkade extends SimplePropertyExpression<Bande, Number> {

    static {
        register(ExprBandeSkade.class, Number.class, "bande[ ]skade", "bande");
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    @Nullable
    public Number convert(Bande bande) {
        return bande.getBandeSkade();
    }

    @Override
    protected String getPropertyName() {
        return "bande[ ]skade";
    }
}