package dk.tandhjulet.skript.events;

import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import dk.tandhjulet.events.BandeKickEvent;

@SuppressWarnings("all")
public class EvtBandeKick extends SkriptEvent {
    static {
        Skript.registerEvent("Bande Kick", EvtBandeKick.class, BandeKickEvent.class, "bande kick");
    }

    @Override
    public String toString(@Nullable Event arg0, boolean arg1) {
        return "bande kick event";
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
