package dk.tandhjulet.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.storage.Message;

public class CommandBAC implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        BandePlayer bandePlayer = BandePlugin.getAPI().getPlayer((Player) sender);
        if (!bandePlayer.hasBande()) {
            Message.sendReplaced(bandePlayer, "bande_command.no_bande", null);
            return true;
        }

        Message.sendReplaced(bandePlayer.getBande().getMemberIterable(), "chat.bac_format", null,
                bandePlayer.getBandeRank().toString(),
                bandePlayer.getBase().getDisplayName(),
                String.join(" ", args));

        return true;
    }

}
