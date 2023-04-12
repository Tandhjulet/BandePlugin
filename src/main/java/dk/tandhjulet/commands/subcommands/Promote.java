package dk.tandhjulet.commands.subcommands;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import com.google.common.collect.ImmutableList;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.enums.BandeRank;
import dk.tandhjulet.events.BandePromoteEvent;
import dk.tandhjulet.storage.Message;

public class Promote {
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
                Message.sendReplaced(sender, "promote.player_not_in_bande", null, playerName);
                return;
            }

            BandePlayer bandePlayer = BandePlugin.getAPI().getPlayer(player.getUniqueId());

            List<UUID> members = ImmutableList.copyOf(sender.getBande().getMemberIterable());
            if (!members.contains(bandePlayer.getBase().getUniqueId())) {
                Message.sendReplaced(sender, "promote.player_not_in_bande", null, playerName);
                return;
            }

            if (sender.getBandeRank().isLowerThanOrEqualTo(bandePlayer.getBandeRank())) {
                Message.sendReplaced(sender, "bande_command.not_enough_authority", null, playerName);
                return;
            }

            Bande bande = sender.getBande();
            BandeRank next = bandePlayer.getBandeRank().next();
            if (next == bandePlayer.getBandeRank()) {
                Message.sendReplaced(sender, "promote.invalid", null, playerName);
                return;
            }

            bande.removeMember(bandePlayer.getBandeRank(), bandePlayer.getBase().getUniqueId());

            bande.addMember(next, bandePlayer.getBase().getUniqueId());
            bandePlayer.setBandeRank(next);

            Message.sendReplaced(bande.getMemberIterable(), "promote.message", null, playerName,
                    next.toString().toLowerCase());

            Bukkit.getPluginManager().callEvent(new BandePromoteEvent(bandePlayer, bande));
        } else {
            Message.sendReplaced(sender, "promote.incorrect_syntax", null);
        }
    }
}
