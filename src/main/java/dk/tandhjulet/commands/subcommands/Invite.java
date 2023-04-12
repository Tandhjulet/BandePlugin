package dk.tandhjulet.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.enums.BandeRank;
import dk.tandhjulet.storage.Message;

public class Invite {
    public static void run(BandePlayer sender, String[] args) {
        if (sender.getBandeRank().isLowerThan(BandeRank.ADMIN)) { // mod eller medlem
            Message.sendReplaced(sender, "general.access_denied", null);
            return;
        }

        if (args.length >= 2) {
            String playerName = args[1];
            Player p = Bukkit.getPlayer(playerName);
            if (p == null || !p.isOnline()) {
                Message.sendReplaced(sender, "invite.not_online", null, playerName);
                return;
            }

            BandePlayer toInvite = BandePlugin.getAPI().getPlayer(p);
            if (toInvite.hasBande()) {
                Message.sendReplaced(sender, "invite.has_bande", null, playerName, toInvite.getBande().getName());
                return;
            }

            sender.getBande().invite(sender.getBase(), p.getUniqueId());
        } else {
            Message.sendReplaced(sender.getBase(), "invite.incorrect_syntax", null);
        }
    }
}
