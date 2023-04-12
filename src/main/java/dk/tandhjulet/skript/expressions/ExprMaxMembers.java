package dk.tandhjulet.skript.expressions;

import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import dk.tandhjulet.bande.Bande;

@SuppressWarnings("all")
public class ExprMaxMembers extends SimplePropertyExpression<Bande, Number> {

    static {
        register(ExprMaxMembers.class, Number.class, "max[(-| )]members", "bande");
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    @Nullable
    public Number convert(Bande bande) {
        return bande.getMaxMembers();
    }

    @Override
    protected String getPropertyName() {
        return "max[(-| )]members";
    }
}