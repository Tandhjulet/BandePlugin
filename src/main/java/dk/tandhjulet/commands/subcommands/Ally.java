package dk.tandhjulet.commands.subcommands;

import org.bukkit.Bukkit;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.enums.BandeRank;
import dk.tandhjulet.events.BandeAllyEvent;
import dk.tandhjulet.storage.Message;

public class Ally {

    public static void run(BandePlayer sender, String[] args) {
        if (sender.getBandeRank().isLowerThan(BandeRank.ADMIN)) { // mod eller medlem
            Message.sendReplaced(sender, "general.access_denied", null);
            return;
        }

        if (args.length >= 2) {
            String bandeName = args[1];

            Bande to = BandePlugin.getAPI().getBande(bandeName);
            if (to == null) {
                Message.sendReplaced(sender, "general.invalid_bande", null);
                return;
            }
            Bande from = sender.getBande();

            if (to.getAllianceInvites().contains(from.getName())) {
                Message.sendReplaced(sender, "ally.already_sent_invite", null, to.getName());
                return;
            }

            if (from.getRivals().contains(to.getName())) {
                Message.sendReplaced(sender, "ally.is_rival", null, from.getName(), to.getName());
                return;
            }
            if (to.getRivals().contains(from.getName())) {
                Message.sendReplaced(sender, "ally.is_rival", null, to.getName(), from.getName());
                return;
            }

            if (from.getAllianceInvites().contains(to.getName())) {
                if (from.getAlliances().contains(to.getName()) || to.getAlliances().contains(from.getName())) {
                    Message.sendReplaced(sender, "ally.already_in_alliance", null, to.getName());
                    return;
                }

                if (from.isMaxAlliances()) {
                    Message.sendReplaced(sender, "ally.max", null, from.getName());
                    return;
                }
                if (to.isMaxAlliances()) {
                    Message.sendReplaced(sender, "ally.max", null, to.getName());
                    return;
                }

                from.addAlliance(to);
                to.addAlliance(from);

                Message.sendReplaced(to.getMemberIterable(), "ally.new_alliance", null, from.getName());
                Message.sendReplaced(from.getMemberIterable(), "ally.new_alliance", null, to.getName());

                from.removeAllianceInvite(to.getName());
                to.removeAllianceInvite(from.getName());

                Bukkit.getPluginManager().callEvent(new BandeAllyEvent(to, from));
                return;
            }

            to.addAllyInvite(from, sender);
        } else {
            Message.sendReplaced(sender, "ally.incorrect_syntax", null);
        }
    }
}
