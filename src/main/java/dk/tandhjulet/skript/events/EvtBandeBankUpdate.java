package dk.tandhjulet.skript.events;

import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import dk.tandhjulet.events.BandeBankUpdateEvent;

@SuppressWarnings("all")
public class EvtBandeBankUpdate extends SkriptEvent {
    static {
        Skript.registerEvent("Bande Bank Update", EvtBandeBankUpdate.class, BandeBankUpdateEvent.class,
                "[bande ]bank[ update]");
    }

    @Override
    public String toString(@Nullable Event arg0, boolean arg1) {
        return "bande bank update event";
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
