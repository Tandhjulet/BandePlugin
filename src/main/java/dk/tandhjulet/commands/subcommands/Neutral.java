package dk.tandhjulet.commands.subcommands;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.enums.BandeRank;
import dk.tandhjulet.storage.Message;

public class Neutral {
    public static void run(BandePlayer sender, String[] args) {
        if (sender.getBandeRank().isLowerThan(BandeRank.ADMIN)) { // mod eller medlem
            Message.sendReplaced(sender, "general.access_denied", null);
            return;
        }

        if (args.length >= 2) {
            String bandeName = args[1];

            Bande other = BandePlugin.getAPI().getBande(bandeName);

            if (sender.getBande().getRivals().contains(other.getName())) {
                sender.getBande().removeRival(other);
            }
            if (sender.getBande().getAlliances().contains(other.getName())) {
                if (other != null) {
                    other.removeAlliance(sender.getBande());
                    other.removeAllianceInvite(sender.getBande().getName());

                    Message.sendReplaced(other.getMemberIterable(), "bande.neutral", null,
                            sender.getBase().getName(), sender.getBande().getName());
                }
                sender.getBande().removeAlliance(other);
                sender.getBande().removeAllianceInvite(bandeName);
            }

            Message.sendReplaced(sender.getBande().getMemberIterable(), "bande.neutral", null,
                    sender.getBase().getName(),
                    bandeName);

        } else {
            Message.sendReplaced(sender, "neutral.incorrect_syntax", null);
        }
    }
}
