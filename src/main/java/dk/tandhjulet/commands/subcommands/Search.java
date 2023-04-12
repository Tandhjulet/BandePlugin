package dk.tandhjulet.commands.subcommands;

import org.bukkit.Bukkit;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.gui.GUI;
import dk.tandhjulet.storage.Message;

public class Search {
    public static void run(BandePlayer sender, String[] args) {
        if (args.length >= 2) {
            String bandeName = args[1];
            Bande bande = BandePlugin.getAPI().getBande(bandeName);
            if (bande == null) {
                Message.sendReplaced(sender.getBase(), "search.bande_findes_ikke", null, bandeName);
                return;
            }

            GUI gui = BandePlugin.getGuiManager().getGUI("bande_search");

            gui.open(sender.getBase(), Bukkit.getOfflinePlayer(bande.getOwner()), false, false);
        } else {
            Message.sendReplaced(sender.getBase(), "search.incorrect_syntax", null);
        }
    }
}
