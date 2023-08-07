package dk.tandhjulet.skript.expressions;

import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import dk.tandhjulet.bande.Bande;

public class ExprBandeHus extends SimplePropertyExpression<Bande, String> {

    static {
        register(ExprBandeHus.class, String.class, "[bande ]hus", "bande");
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    @Nullable
    public String convert(Bande bande) {
        return bande.getBandeHus();
    }

    @Override
    protected String getPropertyName() {
        return "[bande ]hus";
    }

    @Override
    public void change(Event event, Object[] delta, ChangeMode mode) {
        if (delta != null) {
            Bande bande = getExpr().getSingle(event);
            if (mode != ChangeMode.DELETE && mode != ChangeMode.RESET)
                return;

            String cellName = ((String) delta[0]);

            switch (mode) {
                case DELETE:
                    bande.setBandeHus(null);
                    break;
                case SET:
                    bande.setBandeHus(cellName);
                    break;
                default:
                    assert false;
            }
        }
    }

    @Override
    public Class<?>[] acceptChange(final ChangeMode mode) {
        if (mode == ChangeMode.DELETE || mode == ChangeMode.SET) {
            return CollectionUtils.array(String.class);
        }
        return null;
    }
}