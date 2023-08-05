package dk.tandhjulet.commands.subcommands;

import org.bukkit.Bukkit;

import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.enums.BandeRank;
import dk.tandhjulet.events.BandeForladEvent;
import dk.tandhjulet.storage.Message;

public class Forlad {
    public static void run(BandePlayer sender, String[] args) {
        if (sender.getBandeRank() == BandeRank.EJER) {
            Message.sendReplaced(sender, "forlad.bande_ejer", null, args);
            return;
        }

        Bande bande = sender.getBande();

        Message.sendReplaced(bande.getMemberIterable(), "forlad.message", null, sender.getBase().getName());

        BandeRank rank = sender.getBandeRank();
        bande.removeMember(rank, sender.getBase().getUniqueId());

        sender.setBande(null, null);

        Bukkit.getPluginManager().callEvent(new BandeForladEvent(sender.getBase(), bande));
    }
}