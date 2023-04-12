package dk.tandhjulet.skript.expressions;

import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.enums.BandeRank;

@SuppressWarnings("all")
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