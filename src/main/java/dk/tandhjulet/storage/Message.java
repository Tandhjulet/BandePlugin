package dk.tandhjulet.storage;

import java.io.File;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.placeholders.BandePlaceholders;
import dk.tandhjulet.utils.Logger;
import dk.tandhjulet.utils.Utils;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;

public class Message {
    private static HashMap<String, String[]> messages;

    // TODO: revamp this.

    public static void init() {
        messages = new HashMap<>();

        final File file = new File(BandePlugin.getPlugin().getDataFolder(), "messages.yml");
        final YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (final String key : config.getConfigurationSection("").getKeys(true)) {
            if (config.isConfigurationSection(key)) {
                continue;
            }

            if (config.getStringList(key) != null && config.isList(key)) {
                final List<String> stringList = Utils.getColored(config.getStringList(key));
                messages.put(key, stringList.toArray(new String[0]));
            } else {
                if (config.getString(key) == null) {
                    continue;
                }
                final List<String> stringList = Collections.singletonList(Utils.getColored(config.getString(key)));
                messages.put(key, stringList.toArray(new String[0]));
            }
        }
    }

    public static void sendReplaced(UUID uuid, String path, Map.Entry<String, String> action, String... replacements) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        if (player.isOnline()) {
            sendReplaced(player.getPlayer(), path, action, replacements);
        }
    }

    public static void sendReplaced(Player player, String path, Map.Entry<String, String> action,
            String... replacements) {
        if (player == null) {
            return;
        }

        String[] lines = get(path).clone();

        lines = Arrays.stream(lines).map(line -> {
            return format(Utils.getColored(line), (Object[]) Utils.getColored(replacements));
        }).collect(Collectors.toList()).toArray(new String[0]);

        if (action != null) {
            IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"" + String.join("\n", lines)
                    + "\",\"clickEvent\":{\"action\":" + action.getKey() + ",\"value\":\"" + action.getValue()
                    + "\"}}");
            PacketPlayOutChat chat = new PacketPlayOutChat(comp);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(chat);
            return;
        }

        player.sendMessage(lines);
    }

    private static String format(String string, final Object... objects) {
        MessageFormat messageFormat;
        try {
            messageFormat = new MessageFormat(string);
        } catch (final IllegalArgumentException e) {
            Logger.severe("Invalid key " + string + ".");
            string = string.replaceAll("\\{(\\D*?)\\}", "\\[$1\\]");
            messageFormat = new MessageFormat(string);
        }
        return messageFormat.format(objects);
    }

    public static List<Component> getReplaced(String path, String... replacements) {
        String[] lines = get(path).clone();
        List<Component> out = new LinkedList<>();

        List<String> searchFor = new LinkedList<String>() {
            {
                add("{0}");
                add("{1}");
                add("{2}");
                add("{3}");
                add("{4}");
                add("{5}");
                add("{6}");
            }
        };

        String[] l;
        if (replacements != null)
            l = searchFor.subList(0, replacements.length).toArray(new String[0]);
        else
            l = null;

        Arrays.stream(lines).forEach(line -> {
            out.add(Component.text(StringUtils.replaceEach(Utils.getColored(line), l,
                    Utils.getColored(replacements))));
        });

        return out;
    }

    public static List<Component> getPlaceholderReplaced(Bande bande, OfflinePlayer player, String path) {
        if (bande == null) {
            return new LinkedList<>();
        }
        List<String> lines = Arrays.asList(get(path));
        lines = BandePlaceholders.setPlaceholders(bande, lines);
        lines = PlaceholderAPI.setPlaceholders(player, lines);

        List<Component> out = new LinkedList<>();

        for (String line : lines) {
            out.add(Component.text(Utils.getColored(line)));
        }

        return out;
    }

    public static List<Component> getAsComponent(String path) {
        List<String> lines = Arrays.asList(get(path));
        List<Component> out = new LinkedList<>();

        for (String line : lines) {
            out.add(Component.text(Utils.getColored(line)));
        }

        return out;
    }

    public static void sendReplaced(BandePlayer player, String path, Map.Entry<String, String> action,
            String... replacements) {
        sendReplaced(player.getBase(), path, action, replacements);
    }

    public static void sendReplaced(Player[] players, String path, Map.Entry<String, String> action,
            String... replacements) {
        for (Player p : players)
            sendReplaced(p, path, action, replacements);
    }

    public static void sendReplaced(Iterable<UUID> uuids, String path, Map.Entry<String, String> action,
            String... replacements) {
        uuids.forEach(uuid -> {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null) {
                sendReplaced(p, path, action, replacements);
            }
        });
    }

    public static String[] get(String path) {
        if (!messages.containsKey(path)) {
            Logger.warn("Missing '" + path
                    + "' in messages.yml. Messages.yml was likely created by an outdated version of the plugin. Trying to recover from embedded file.");
            return new String[] { "" };
        }
        return messages.get(path);
    }

    public static void send(final Player player, final String path) {
        player.sendMessage(get(path));
    }
}
