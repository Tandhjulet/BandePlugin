package dk.tandhjulet.skript.expressions;

import org.jetbrains.annotations.Nullable;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import dk.tandhjulet.bande.Bande;

public class ExprBandeName extends SimplePropertyExpression<Bande, String> {

    static {
        register(ExprBandeName.class, String.class, "name", "bande");
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    @Nullable
    public String convert(Bande bande) {
        return bande.getName();
    }

    @Override
    protected String getPropertyName() {
        return "name";
    }
}