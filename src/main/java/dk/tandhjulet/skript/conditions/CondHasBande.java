package dk.tandhjulet.skript.conditions;

import javax.annotation.Nonnull;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import dk.tandhjulet.BandePlugin;

public class CondHasBande extends Condition {

    static {
        Skript.registerCondition(CondHasBande.class, "%offlineplayer% has [a ]bande");
    }

    Expression<OfflinePlayer> player;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(@Nonnull Expression<?>[] arg0, int arg1, @Nonnull Kleenean arg2, @Nonnull ParseResult arg3) {
        player = (Expression<OfflinePlayer>) arg0[0];
        return true;
    }

    @Override
    @Nonnull
    public String toString(@Nonnull Event arg0, boolean arg1) {
        return player.toString(arg0, arg1) + " has bande";
    }

    @Override
    public boolean check(@Nonnull Event arg0) {
        OfflinePlayer p = this.player.getSingle(arg0);
        return BandePlugin.getAPI().getPlayer(p.getUniqueId()).hasBande();
    }

}
