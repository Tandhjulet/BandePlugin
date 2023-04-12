package dk.tandhjulet.commands;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.api.nbt.NBTAPI;
import dk.tandhjulet.utils.Utils;
import net.kyori.adventure.text.Component;

public class CommandItemBuilder implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Command cannot be executed by console.");
            return true;
        }
        Player player = (Player) sender;

        if (!player.isOp()) {
            sender.sendMessage("Du har ikke adgang til at bruge denne kommando.");
            return true;
        }

        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            player.sendMessage("Du skal holde et item i hånden, for at kunne bruge itembuilderen.");
            return true;
        }

        // change name

        if (args.length >= 1 && args[0].equalsIgnoreCase("name")) {
            List<String> strs = Arrays.asList(args).subList(1, args.length);

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(Utils.getColored(String.join(" ", strs)));
            item.setItemMeta(meta);

            player.setItemInHand(item);
            return true;
        }

        // change dynamic player skull

        if (args.length >= 2 && args[0].equalsIgnoreCase("playerskull") && args[1].equalsIgnoreCase("on")) {
            player.setItemInHand(NBTAPI.setTag("player-skull", "on", item));
            return true;
        } else if (args.length >= 2 && args[0].equalsIgnoreCase("playerskull") && args[1].equalsIgnoreCase("off")) {
            player.setItemInHand(NBTAPI.removeTag("player-skull", item));
            return true;
        }

        // change lore

        if (args.length >= 2 && args[0].equalsIgnoreCase("lore") && args[1].equalsIgnoreCase("remove")) {
            ItemMeta meta = item.getItemMeta();
            if (!meta.hasLore()) {
                return true;
            }

            meta.setLore(meta.getLore().subList(0, meta.getLore().size() - 1));
            item.setItemMeta(meta);

            player.setItemInHand(item);
            return true;
        } else if (args.length >= 3 && args[0].equalsIgnoreCase("lore") && args[1].equalsIgnoreCase("add")) {
            List<String> strs = Arrays.asList(args).subList(2, args.length);

            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.getLore();
            if (lore == null)
                lore = new LinkedList<>();

            String toAdd = Utils.getColored(String.join(" ", strs));
            lore.add(toAdd);

            meta.setLore(lore);
            item.setItemMeta(meta);

            player.setItemInHand(item);
            return true;
        }

        // change type

        if (args.length >= 1 && args[0].equalsIgnoreCase("type")) {
            Set<String> values = BandePlugin.getTypeManager().values();

            Gui newGui = Gui.gui()
                    .title(Component.text("Select type"))
                    .rows(6).create();

            AtomicInteger index = new AtomicInteger(-1);
            values.forEach(s -> {
                newGui.setItem(index.incrementAndGet(),
                        ItemBuilder.from(Material.SIGN).name(Component.text(Utils.getColored(s))).asGuiItem(click -> {
                            String type = Utils.uncolored(click.getCurrentItem().getItemMeta().getDisplayName());

                            ItemStack i = NBTAPI.setTag("bande-type", type, item);
                            click.getWhoClicked().setItemInHand(i);

                            click.setCancelled(true);
                            Utils.scheduleClose(click);
                        }));
            });

            newGui.open(player);
            return true;
        }

        // change bande område type

        if (args.length >= 2 && args[0].equalsIgnoreCase("bandeomraade")) {
            if (args[1].equalsIgnoreCase("type")) {
                String type = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

                ItemStack i = NBTAPI.setTag("omraade-type", type, item);
                player.setItemInHand(i);

                player.sendMessage("Skiftede bande-område typen af dit item til " + type);
            } else if (args[1].equalsIgnoreCase("price")) {
                String type = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

                ItemStack i = NBTAPI.setTag("omraade-price", type, item);
                player.setItemInHand(i);

                player.sendMessage("Skiftede bande-område adgangsprisen af dit item til " + type);
            }

            return true;
        }

        // glow

        if (args.length >= 1 && args[0].equalsIgnoreCase("glow")) {
            if (item.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS)
                    && item.getItemMeta().hasEnchant(Enchantment.LURE)) {
                player.setItemInHand(
                        ItemBuilder.from(item).glow(false).build());
            } else {
                player.setItemInHand(
                        ItemBuilder.from(item).glow(true).build());
            }
            return true;
        }

        player.sendMessage(Utils.getColored("Forkert brug."));
        player.sendMessage(Utils.getColored("/itembuilder glow"));
        player.sendMessage("/itembuilder bandeomraade <type/price> <værdi>");
        player.sendMessage(Utils.getColored("/itembuilder playerskull <on/off>"));
        player.sendMessage(Utils.getColored("/itembuilder type"));
        player.sendMessage(Utils.getColored("/itembuilder lore <add/remove> [lore]"));
        player.sendMessage(Utils.getColored("/itembuilder name <navn>"));
        return true;
    }
}
