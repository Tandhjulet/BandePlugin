package dk.tandhjulet.commands.subcommands;

import org.bukkit.Bukkit;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.enums.BandeRank;
import dk.tandhjulet.events.BandeRivalEvent;
import dk.tandhjulet.storage.Message;

public class Rival {
    public static void run(BandePlayer sender, String[] args) {
        if (sender.getBandeRank().isLowerThan(BandeRank.ADMIN)) { // mod eller medlem
            Message.sendReplaced(sender, "general.access_denied", null);
            return;
        }

        if (args.length >= 2) {
            String bandeName = args[1];

            Bande bande = BandePlugin.getAPI().getBande(bandeName);
            if (bande == null) {
                Message.sendReplaced(sender, "general.invalid_bande", null);
                return;
            }
            if (bande == sender.getBande()) {
                Message.sendReplaced(sender, "rival.rival_own_bande", null);
                return;
            }
            if (bande.isMaxRivals()) {
                Message.sendReplaced(sender, "rival.max", null);
                return;
            }

            Bande from = sender.getBande();
            if (from.getAlliances().contains(bande.getName())) {
                Message.sendReplaced(sender, "rival.is_ally", null, bande.getName());
                return;
            }

            if (from.getRivals().contains(bandeName)) {
                Message.sendReplaced(sender, "rival.already_rival", null, bande.getName());
                return;
            }

            from.addRival(bande);
            Message.sendReplaced(sender.getBande().getMemberIterable(), "rival.new_rival", null, bandeName);

            Bukkit.getPluginManager().callEvent(new BandeRivalEvent(sender.getBande(), bande));
        } else {
            Message.sendReplaced(sender, "rival.incorrect_syntax", null);
        }
    }
}
