package dk.tandhjulet.skript.effects;

import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.gui.GUI;

@SuppressWarnings("all")
public class EffRerenderGUI extends Effect {
    static {
        Skript.registerEffect(EffRerenderGUI.class,
                "[re]render [bande ](gui|inventory) %string%");
    }

    private Expression<String> guiName;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        this.guiName = (Expression<String>) expressions[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "render gui " + guiName.toString(event, debug);
    }

    @Override
    protected void execute(Event event) {
        String gui = guiName.getSingle(event);
        if (gui == null)
            return;

        GUI GUI = BandePlugin.getGuiManager().getGUI(gui.toLowerCase());
        if (GUI == null)
            return;

        GUI.init();
    }
}
