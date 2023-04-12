package dk.tandhjulet.commands.subcommands;

import org.bukkit.Bukkit;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.enums.BandeRank;
import dk.tandhjulet.events.BandeJoinEvent;
import dk.tandhjulet.storage.Message;

public class Join {
    public static void run(BandePlayer sender, String[] args) {
        if (args.length >= 2) {
            if (sender.hasBande()) {
                Message.sendReplaced(sender.getBase(), "join.has_bande", null, args[1]);
                return;
            }

            Bande bande = BandePlugin.getAPI().getBande(args[1]);
            if (bande == null) {
                Message.sendReplaced(sender.getBase(), "general.invalid_bande", null);
                return;
            }

            if (!sender.getInvitations().contains(args[1])) {
                Message.sendReplaced(sender.getBase(), "join.not_invited", null, args[1]);
                return;
            }

            if (bande.getMaxMembers() == bande.getMemberIterable().spliterator().getExactSizeIfKnown()) {
                Message.sendReplaced(sender, "join.max_members", null);
                return;
            }

            bande.addMember(BandeRank.MEDLEM, sender.getBase().getUniqueId());
            sender.setBande(bande.getName(), BandeRank.MEDLEM);

            sender.clearInvitations();
            bande.removeInvite(sender.getBase().getUniqueId());

            sender.forceSave();
            bande.forceSave();

            Message.sendReplaced(bande.getMemberIterable(), "join.new_join", null, sender.getBase().getName());

            Bukkit.getPluginManager().callEvent(new BandeJoinEvent(sender.getBase(), bande));

        } else {
            Message.sendReplaced(sender.getBase(), "join.incorrect_syntax", null);
        }
    }
}
