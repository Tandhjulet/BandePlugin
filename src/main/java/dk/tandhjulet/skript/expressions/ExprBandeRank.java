package dk.tandhjulet.skript.expressions;

import org.jetbrains.annotations.Nullable;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.enums.BandeRank;

public class ExprBandeRank extends SimplePropertyExpression<BandePlayer, BandeRank> {

    static {
        register(ExprBandeRank.class, BandeRank.class, "[bande ]rank", "bandeplayer");
    }

    @Override
    public Class<? extends BandeRank> getReturnType() {
        return BandeRank.class;
    }

    @Override
    @Nullable
    public BandeRank convert(BandePlayer bande) {
        return bande.getBandeRank();
    }

    @Override
    protected String getPropertyName() {
        return "[bande ]rank";
    }
}