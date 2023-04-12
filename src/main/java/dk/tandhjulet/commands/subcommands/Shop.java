package dk.tandhjulet.commands.subcommands;

import org.bukkit.entity.Player;

import dk.tandhjulet.BandePlugin;

public class Shop {
    public static void run(Player player) {
        BandePlugin.getAPI().openGUI(player, "bande_shop");
    }
}
