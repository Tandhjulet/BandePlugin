package dk.tandhjulet.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryInteractEvent;

import dk.tandhjulet.BandePlugin;

public class Utils {

    public static String getColored(final String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static List<String> getColored(final List<String> str) {
        str.replaceAll(e -> {
            return getColored(e);
        });

        return str;
    }

    public static String[] getColored(final String... str) {
        if (str == null) {
            return new String[0];
        }
        List<String> stringList = Arrays.stream(str).map(e -> {
            return getColored(e);
        }).collect(Collectors.toList());

        return stringList.toArray(new String[0]);
    }

    public static void scheduleClose(InventoryInteractEvent e) {

        Bukkit.getScheduler().runTask(BandePlugin.getPlugin(), new Runnable() {
            @Override
            public void run() {
                e.getWhoClicked().closeInventory();
            }
        });
    }

    public static String uncolored(final String str) {
        return ChatColor.stripColor(str);
    }

    public static String kebabCase(final String str) {
        return str.replaceAll("([A-Z])", "-$1").toLowerCase();
    }
}
