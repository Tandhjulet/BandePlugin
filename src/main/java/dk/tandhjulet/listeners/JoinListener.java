package dk.tandhjulet.listeners;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.google.common.collect.ImmutableList;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.storage.Message;
import dk.tandhjulet.update.UpdateChecker;

public class JoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        BandePlayer player = BandePlugin.getAPI().getPlayer(event.getPlayer()); // verify that user is loaded, and load
                                                                                // into cache.

        if (player.hasBande()) {
            List<UUID> members = ImmutableList.copyOf(player.getBande().getMemberIterable());
            if (!members.contains(player.getBase().getUniqueId())) {
                player.setBande(null, null);
            }
        }

        if (event.getPlayer().isOp()) {
            if (UpdateChecker.errored() || !UpdateChecker.isUpdateAvailable()) {
                return;
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask(BandePlugin.getPlugin(), new Runnable() {

                @Override
                public void run() {
                    Message.sendReplaced(event.getPlayer(), "general.update", null, UpdateChecker.getPluginVersion(),
                            UpdateChecker.getNewestVersion());
                }

            });
        }
    }
}
