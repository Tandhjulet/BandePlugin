package dk.tandhjulet.listeners;

import java.util.List;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.google.common.collect.ImmutableList;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.BandePlayer;

public class JoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        BandePlayer player = BandePlugin.getAPI().getPlayer(event.getPlayer()); // verify that user is loaded, and load
                                                                                // into cache.
        player.init();
        player.setBase(event.getPlayer());

        if (player.hasBande()) {
            List<UUID> members = ImmutableList.copyOf(player.getBande().getMemberIterable());
            if (!members.contains(player.getBase().getUniqueId())) {
                player.setBande(null, null);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // gracefully unload user to avoid memory leaks

        BandePlayer player = BandePlugin.getAPI().getPlayer(event.getPlayer());

        if (player.isDirty()) {
            player.setDirty(false);
            player.forceSave();
        }
        player.getTimer().cancel();
    }
}
