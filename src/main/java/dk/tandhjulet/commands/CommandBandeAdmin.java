package dk.tandhjulet.commands;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.level.LevelManager;
import dk.tandhjulet.storage.Message;
import dk.tandhjulet.utils.Utils;

public class CommandBandeAdmin implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(Utils.getColored("&cForkert syntaks.", "&c/bandeadmin updatetop",
                    "&c/bandeadmin disband <bande navn>", "&c/bandeadmin reload",
                    "&c/bandeadmin bandehus (add|remove) <region navn>", "&c/itembuilder",
                    "&c/bande editgui", "&c/bande setguiname <gui_id> <nye_navn>"));
            return true;
        }

        if (args[0].equalsIgnoreCase("updatetop")) {
            BandePlugin.getTop().sortFangeKills();
            BandePlugin.getTop().sortLevels();
            BandePlugin.getTop().sortOffiKills();
            BandePlugin.getTop().sortVagtKills();
            sender.sendMessage("Updated top.");
        }

        if (args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(Utils.getColored("&aReloader beskeder, levels, shop cooldowns og konfiguration..."));
            BandePlugin.getConfiguration().reload();
            Message.init();
            LevelManager.reload();
            BandePlugin.getTypeManager().reloadCooldowns();
            sender.sendMessage(Utils.getColored("&aReloadede beskeder, levels, shop cooldowns og konfiguration."));
        }

        if (args.length >= 2 && args[0].equalsIgnoreCase("disband")) {
            String bandeName = args[1];
            Bande bande = BandePlugin.getAPI().getBande(bandeName);
            if (bande == null) {
                sender.sendMessage(ChatColor.RED + "Banden findes ikke.");
                return true;
            }

            for (UUID uuid : bande.getMemberIterable()) {
                BandePlayer player = BandePlugin.getAPI().getPlayer(uuid);
                player.setBande(null, null);
                player.forceSave();

                Message.sendReplaced(player.getBase(), "disband.force_message", null, sender.getName());
            }

            bande.invalidate();
            BandePlugin.getFileManager().removeBande(bande.getName());
            sender.sendMessage(ChatColor.RED + "Du opløste banden " + bandeName + ".");
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("setguiname")) {
            if (args.length >= 3) {
                if (!BandePlugin.getInventoryDataHolder().getIds().contains(args[1].toLowerCase())) {
                    sender.sendMessage(ChatColor.RED + args[1] + " er ikke et validt gui id");
                    sender.sendMessage(ChatColor.RED + "/bande setguiname <gui_id> <nye_navn>");
                    sender.sendMessage(ChatColor.RED + "Du kan finde en liste af alle GUI ID'er i /bande editgui.");
                    return true;
                }

                String guiName = String.join(" ", Arrays.asList(args).subList(2, args.length));
                BandePlugin.getInventoryDataHolder().setName(args[1].toLowerCase(), guiName);
            } else {
                sender.sendMessage(ChatColor.RED + "/bande setguiname <gui_id> <nye_navn>");
                sender.sendMessage(ChatColor.RED + "Du kan finde en liste af alle GUI ID'er i /bande editgui.");
            }
        }

        if (args.length >= 3 && args[0].equalsIgnoreCase("bandehus") && args[1].equalsIgnoreCase("add")) {
            if (!BandePlugin.getHouseManager().isEnabled()) {
                sender.sendMessage(
                        "Bandehus-funktionen er ikke aktiveret. Se logsne for mere information; kontakt sælger hvis du mangler hjælp.");
                return true;
            }

            String cellName = args[2];

            if (!BandePlugin.getHouseManager().exists(cellName)) {
                sender.sendMessage("Der findes ingen celle ved navn " + cellName + "!");
                return true;
            }

            BandePlugin.getHouseHolder().add(cellName);
            sender.sendMessage("Du tilføjede cellen " + cellName + " til bande husene.");
        } else if (args.length >= 3 && args[0].equalsIgnoreCase("bandehus") && args[1].equalsIgnoreCase("remove")) {
            String cellName = args[2];

            BandePlugin.getHouseHolder().remove(cellName);
            sender.sendMessage("Du fjernede " + cellName + " fra bande husene.");
        }

        return true;
    }
}