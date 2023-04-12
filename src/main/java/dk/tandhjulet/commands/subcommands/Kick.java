package dk.tandhjulet.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.enums.BandeRank;
import dk.tandhjulet.events.BandeKickEvent;
import dk.tandhjulet.storage.Message;

public class Kick {

    public static void run(BandePlayer sender, String[] args) {
        if (sender.getBandeRank().isLowerThan(BandeRank.ADMIN)) { // mod eller medlem
            Message.sendReplaced(sender, "general.access_denied", null);
            return;
        }

        if (args.length >= 2) {

            String playerName = args[1];

            @SuppressWarnings("deprecation")
            OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);

            if (!player.hasPlayedBefore() && !player.isOnline()) {
                Message.sendReplaced(sender, "kick.invalid_player", null, playerName);
                return;
            }

            sender.getBande().kick(sender, player.getUniqueId());

            Bukkit.getPluginManager().callEvent(new BandeKickEvent(BandePlugin.getAPI().getPlayer(player.getUniqueId()),
                    sender, sender.getBande()));
        } else {
            Message.sendReplaced(sender.getBase(), "kick.incorrect_syntax", null);
        }
    }
}
