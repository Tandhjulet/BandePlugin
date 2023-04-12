package dk.tandhjulet.listeners;

import java.util.Arrays;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.BandePlayer;

public class DeathListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent e) {
        if (e.getEntity().getKiller() == null) {
            return;
        }

        BandePlayer killer = BandePlugin.getAPI().getPlayer(e.getEntity().getKiller());

        List<String> groups = Arrays.asList(BandePlugin.getChat().getPlayerGroups(e.getEntity()));

        List<String> vagtGroups = BandePlugin.getConfiguration().getVagtGroup();
        vagtGroups.retainAll(groups);

        List<String> officerGroup = BandePlugin.getConfiguration().getOfficerGroup();
        officerGroup.retainAll(groups);

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
