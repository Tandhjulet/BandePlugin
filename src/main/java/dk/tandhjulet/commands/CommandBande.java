package dk.tandhjulet.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.commands.subcommands.Ally;
import dk.tandhjulet.commands.subcommands.Bank;
import dk.tandhjulet.commands.subcommands.Demote;
import dk.tandhjulet.commands.subcommands.Disband;
import dk.tandhjulet.commands.subcommands.EditGUI;
import dk.tandhjulet.commands.subcommands.Forlad;
import dk.tandhjulet.commands.subcommands.Invite;
import dk.tandhjulet.commands.subcommands.Join;
import dk.tandhjulet.commands.subcommands.Kick;
import dk.tandhjulet.commands.subcommands.Neutral;
import dk.tandhjulet.commands.subcommands.Promote;
import dk.tandhjulet.commands.subcommands.Rival;
import dk.tandhjulet.commands.subcommands.Search;
import dk.tandhjulet.storage.Message;
import dk.tandhjulet.utils.Logger;

public class CommandBande implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Logger.warn("You cannot access this as console.");
            return false;
        }
        BandePlayer bandePlayer = BandePlugin.getAPI().getPlayer((Player) sender);

        if (args.length == 0) {
            if (sender instanceof Player) {

                if (bandePlayer.hasBande()) {
                    BandePlugin.getAPI().openGUI((Player) sender, "hjem_med_bande");
                } else {
                    BandePlugin.getAPI().openGUI((Player) sender, "hjem_ingen_bande");
                }

            } else {
                Logger.warn("You cannot access GUIs as console. Use /bande ? for a list of commands.");
                return false;
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("editgui")) {
            if (sender instanceof Player) {
                EditGUI.run((Player) sender);
            } else {
                Logger.warn("You cannot access GUIs as console. Use /bande ? for a list of commands.");
                return false;
            }
            return true;
        }

        else if (args[0].equalsIgnoreCase("join")) {
            Join.run(bandePlayer, args);
            return true;
        }

        else if (args[0].equalsIgnoreCase("search")) {
            Search.run(bandePlayer, args);
            return true;
        }

        // bande required for following commands:

        if (!bandePlayer.hasBande()) {
            Message.sendReplaced(bandePlayer, "bande_command.invalid_command", null);
            return true;
        }

        if (args[0].equalsIgnoreCase("invite")) {
            Invite.run(bandePlayer, args);
            return true;
        }

        else if (args[0].equalsIgnoreCase("ally")) {
            Ally.run(bandePlayer, args);
            return true;
        }

        else if (args[0].equalsIgnoreCase("rival")) {
            Rival.run(bandePlayer, args);
            return true;
        }

        else if (args[0].equalsIgnoreCase("deposit")) {
            Bank.run(bandePlayer, args);
            return true;
        }

        else if (args[0].equalsIgnoreCase("forlad")) {
            Forlad.run(bandePlayer, args);
            return true;
        }

        else if (args[0].equalsIgnoreCase("promote")) {
            Promote.run(bandePlayer, args);
            return true;
        }

        else if (args[0].equalsIgnoreCase("demote")) {
            Demote.run(bandePlayer, args);
            return true;
        }

        else if (args[0].equalsIgnoreCase("disband")) {
            Disband.run(bandePlayer, args);
            return true;
        }

        else if (args[0].equalsIgnoreCase("neutral")) {
            Neutral.run(bandePlayer, args);
            return true;
        }

        else if (args[0].equalsIgnoreCase("kick")) {
            Kick.run(bandePlayer, args);
            return true;
        }

        Message.sendReplaced(bandePlayer, "bande_command.invalid_command", null);
        Message.sendReplaced(bandePlayer, "bande_command.invalid_command_with_bande", null);

        return true;
    }

}