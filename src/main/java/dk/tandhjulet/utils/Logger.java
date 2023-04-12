package dk.tandhjulet.utils;

import java.util.Arrays;

import dk.tandhjulet.BandePlugin;

public class Logger {

    public static void info(String... string) {
        Arrays.asList(string).forEach(s -> info(s));
    }

    public static void info(String string) {
        BandePlugin.getPlugin().getLogger().info(string);
    }

    public static void warn(String string) {
        BandePlugin.getPlugin().getLogger().warning(string);
    }

    public static void severe(String string) {
        BandePlugin.getPlugin().getLogger().severe(string);
    }

}
