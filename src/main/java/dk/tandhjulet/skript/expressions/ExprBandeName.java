package dk.tandhjulet.skript.expressions;

import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import dk.tandhjulet.bande.Bande;

@SuppressWarnings("all")
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