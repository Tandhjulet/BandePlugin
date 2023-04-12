package dk.tandhjulet.skript.events;

import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import dk.tandhjulet.events.BandeGUICloseEvent;
import dk.tandhjulet.events.BandeGUIOpenEvent;
import dk.tandhjulet.events.BandePromoteEvent;

@SuppressWarnings("all")
public class EvtGuiClose extends SkriptEvent {

    static {
        Skript.registerEvent("GUI Close Event", EvtGuiClose.class, BandeGUICloseEvent.class, "[bande ]gui close");
    }

    @Override
    public String toString(@Nullable Event arg0, boolean arg1) {
        return "GUI Close Event";
    }

    @Override
    public boolean check(Event arg0) {
        return true;
    }

    @Override
    public boolean init(Literal<?>[] arg0, int arg1, ParseResult arg2) {
        return true;
    }
}
