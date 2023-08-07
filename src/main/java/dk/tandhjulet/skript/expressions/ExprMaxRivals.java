package dk.tandhjulet.skript.expressions;

import org.jetbrains.annotations.Nullable;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import dk.tandhjulet.bande.Bande;

public class ExprMaxRivals extends SimplePropertyExpression<Bande, Number> {

    static {
        register(ExprMaxRivals.class, Number.class, "max[(-| )]rivals", "bande");
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    @Nullable
    public Number convert(Bande bande) {
        return bande.getMaxRivals();
    }

    @Override
    protected String getPropertyName() {
        return "max[(-| )]rivals";
    }
}