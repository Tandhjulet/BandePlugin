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
import dk.tandhjulet.events.BandeDemoteEvent;
import dk.tandhjulet.storage.Message;

public class Demote {
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
                Message.sendReplaced(sender, "demote.player_not_in_bande", null, playerName);
                return;
            }

            BandePlayer bandePlayer = BandePlugin.getAPI().getPlayer(player.getUniqueId());

            List<UUID> members = ImmutableList.copyOf(sender.getBande().getMemberIterable());
            if (!members.contains(bandePlayer.getBase().getUniqueId())) {
                Message.sendReplaced(sender, "demote.player_not_in_bande", null, playerName);
                return;
            }

            if (!sender.getBandeRank().isHigherThan(bandePlayer.getBandeRank())) {
                Message.sendReplaced(sender, "bande_command.not_enough_authority", null, playerName);
                return;
            }

            Bande bande = sender.getBande();
            BandeRank prev = bandePlayer.getBandeRank().prev();
            if (prev == bandePlayer.getBandeRank()) {
                Message.sendReplaced(sender, "demote.invalid", null, playerName);
                return;
            }

            bande.removeMember(bandePlayer.getBandeRank(), bandePlayer.getBase().getUniqueId());

            bande.addMember(prev, bandePlayer.getBase().getUniqueId());
            bandePlayer.setBandeRank(prev);

            Message.sendReplaced(bande.getMemberIterable(), "demote.message", null, playerName,
                    prev.toString().toLowerCase());

            Bukkit.getPluginManager().callEvent(new BandeDemoteEvent(bandePlayer, bande));
        } else {
            Message.sendReplaced(sender, "demote.incorrect_syntax", null);
        }
    }
}
