package dk.tandhjulet.listeners;

import java.util.Arrays;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.utils.Logger;

public class DeathListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent e) {
        if (e.getEntity().getKiller() == null) {
            return;
        }

        BandePlayer killer = BandePlugin.getAPI().getPlayer(e.getEntity().getKiller());

        List<String> groups = Arrays.asList(BandePlugin.getChat().getPlayerGroups(e.getEntity()));

        Logger.info(groups.toArray(new String[0]));

        List<String> vagtGroups = BandePlugin.getConfiguration().getVagtGroup();
        vagtGroups.retainAll(groups);

        Logger.info(vagtGroups.toArray(new String[0]));

        List<String> officerGroup = BandePlugin.getConfiguration().getOfficerGroup();
        officerGroup.retainAll(groups);

        Logger.info(officerGroup.toArray(new String[0]));

        if (killer.hasBande()) {
            if (officerGroup.size() >= 1) {
                BandePlugin.getTop().addOffiKill(killer.getBande().getName());
            } else if (vagtGroups.size() >= 1) {
                BandePlugin.getTop().addVagtKill(killer.getBande().getName());
            } else {
                BandePlugin.getTop().addFangeKill(killer.getBande().getName());
            }
        }
    }
}
