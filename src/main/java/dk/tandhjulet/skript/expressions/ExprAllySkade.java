package dk.tandhjulet.skript.expressions;

import org.jetbrains.annotations.Nullable;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import dk.tandhjulet.bande.Bande;

public class ExprAllySkade extends SimplePropertyExpression<Bande, Number> {

    static {
        register(ExprAllySkade.class, Number.class, "ally[ ]skade", "bande");
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    @Nullable
    public Number convert(Bande bande) {
        return bande.getAllySkade();
    }

    @Override
    protected String getPropertyName() {
        return "ally[ ]skade";
    }
}