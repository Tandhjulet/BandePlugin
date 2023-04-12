package dk.tandhjulet.commands.subcommands;

import java.util.Timer;
import java.util.UUID;

import org.bukkit.Bukkit;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.enums.BandeRank;
import dk.tandhjulet.events.BandePreDisbandEvent;
import dk.tandhjulet.storage.Message;

public class Disband {
    public static void run(BandePlayer sender, String[] args) {
        if (sender.getBandeRank() != BandeRank.EJER) {
            Message.sendReplaced(sender, "bande_command.access_denied", null);
            return;
        }

        Bande bande = sender.getBande();

        bande.getAlliances().forEach(e -> {
            bande.removeAlliance(BandePlugin.getAPI().getBande(e));
            bande.removeAllianceInvite(e);
        });

        bande.getRivals().forEach(e -> {
            bande.removeRival(BandePlugin.getAPI().getBande(e));
        });

        Bukkit.getPluginManager().callEvent(new BandePreDisbandEvent(sender.getBase(), bande));

        for (UUID uuid : bande.getMemberIterable()) {
            BandePlayer player = BandePlugin.getAPI().getPlayer(uuid);
            player.setBande(null, null);
            player.forceSave();

            Message.sendReplaced(player.getBase(), "disband.message", null, sender.getBase().getName());
        }

        Timer bandeTimer = bande.getTimer();
        if (bandeTimer != null)
            bandeTimer.cancel();

        BandePlugin.getTop().removeBande(bande.getName());
        bande.invalidate();
        BandePlugin.getFileManager().removeBande(bande.getName());
    }
}
