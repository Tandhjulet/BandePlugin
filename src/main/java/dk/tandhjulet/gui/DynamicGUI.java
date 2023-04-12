package dk.tandhjulet.gui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.commands.subcommands.Join;
import dk.tandhjulet.commands.subcommands.Neutral;
import dk.tandhjulet.enums.BandeRank;
import dk.tandhjulet.storage.Message;
import dk.tandhjulet.utils.Logger;
import dk.tandhjulet.utils.Utils;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;

public class DynamicGUI {
    public static GuiItem[] populateInvites(BandePlayer player) {
        List<GuiItem> items = new LinkedList<>();

        HashSet<String> invites = player.getInvitations();

        if (invites.size() > 0) {
            invites.forEach(bande_id -> {
                Bande bande = BandePlugin.getAPI().getBande(bande_id);
                if (bande == null) {
                    player.removeInvitation(bande_id);
                    return;
                }

                // Will fail if bande-owner cannot be found.
                try {
                    List<Component> replacedLore = Message.getPlaceholderReplaced(bande, null, "dynamic_items.invites");
                    Component name = Message.getPlaceholderReplaced(bande, null, "dynamic_items.invites_name").get(0);

                    OfflinePlayer p = Bukkit.getOfflinePlayer(bande.getOwner());

                    GuiItem item = ItemBuilder.skull()
                            .owner(p)
                            .name(name)
                            .lore(replacedLore)
                            .asGuiItem(e -> {
                                Utils.scheduleClose(e);

                                Join.run(player, new String[] { "", bande.getName() });
                            });

                    items.add(item);
                } catch (Exception e) {
                    Logger.severe("An error occured whilst loading bande. Please report this to the developer.");
                    e.printStackTrace();
                }
            });
        }

        return items.toArray(new GuiItem[0]);
    }

    public static GuiItem[] populateMembers(Bande bande, Player player) {
        List<GuiItem> items = new LinkedList<>();

        HashMap<BandeRank, HashSet<UUID>> members = bande.getMembers();

        for (Map.Entry<BandeRank, HashSet<UUID>> entry : members.entrySet()) {
            for (UUID uuid : entry.getValue()) {
                OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
                BandePlayer bandePlayer = BandePlugin.getAPI().getPlayer(p.getUniqueId());

                List<Component> replacedLore = Message.getPlaceholderReplaced(bande, bandePlayer.getBase(),
                        "dynamic_items.member_lore");
                Component name = Message
                        .getPlaceholderReplaced(bande, bandePlayer.getBase(), "dynamic_items.member_name_color").get(0);

                GuiItem item = ItemBuilder.skull()
                        .owner(p)
                        .name(name.append(Component.text(p.getName())))
                        .lore(replacedLore)
                        .asGuiItem(e -> {
                            GUI gui = BandePlugin.getGuiManager().getGUI("bande_manage_member");
                            gui.setOffender(uuid);
                            gui.open(player, player, false, true);
                        });

                items.add(item);
            }
        }

        return items.toArray(new GuiItem[0]);
    }

    public static GuiItem[] populateAllys(Bande bande, BandePlayer sender) {
        List<GuiItem> items = new LinkedList<>();

        for (String bandeName : bande.getAlliances()) {
            Bande b = BandePlugin.getAPI().getBande(bandeName);
            OfflinePlayer p = Bukkit.getOfflinePlayer(b.getOwner());

            BandePlayer bandePlayer = BandePlugin.getAPI().getPlayer(p.getUniqueId());

            List<Component> replacedLore = Message.getPlaceholderReplaced(bande, bandePlayer.getBase(),
                    "dynamic_items.ally_lore");
            Component name = Message
                    .getPlaceholderReplaced(bande, bandePlayer.getBase(), "dynamic_items.ally_name_color").get(0);

            GuiItem item = ItemBuilder.skull()
                    .owner(p)
                    .name(name.append(Component.text(p.getName())))
                    .lore(replacedLore)
                    .asGuiItem(e -> {
                        Utils.scheduleClose(e);
                        Neutral.run(sender, new String[] { "", bandeName });
                    });

            items.add(item);
        }

        return items.toArray(new GuiItem[0]);
    }

    public static GuiItem[] populateRivals(Bande bande, BandePlayer sender) {
        List<GuiItem> items = new LinkedList<>();

        for (String bandeName : bande.getRivals()) {
            Bande b = BandePlugin.getAPI().getBande(bandeName);
            OfflinePlayer p = Bukkit.getOfflinePlayer(b.getOwner());

            BandePlayer bandePlayer = BandePlugin.getAPI().getPlayer(p.getUniqueId());

            List<Component> replacedLore = Message.getPlaceholderReplaced(bande, bandePlayer.getBase(),
                    "dynamic_items.rivals_lore");
            Component name = Message
                    .getPlaceholderReplaced(bande, bandePlayer.getBase(), "dynamic_items.rival_name_color").get(0);

            GuiItem item = ItemBuilder.skull()
                    .owner(p)
                    .name(name.append(Component.text(p.getName())))
                    .lore(replacedLore)
                    .asGuiItem(e -> {
                        Utils.scheduleClose(e);
                        Neutral.run(sender, new String[] { "", bandeName });
                    });

            items.add(item);
        }

        return items.toArray(new GuiItem[0]);
    }

    public static GuiItem[] populateHouses(Bande bande, BandePlayer sender) {
        if (!BandePlugin.getHouseManager().isEnabled()) {
            return null;
        }

        List<GuiItem> items = new LinkedList<>();

        Material mat = Material.STONE;
        try {
            mat = Material.valueOf(Message.get("bande_huse.item_type")[0]);
        } catch (Exception e) {
            return null;
        }

        for (String regionName : BandePlugin.getHouseManager().getAvailableRegions(sender.getBase().getWorld())) {
            Component name = Message.getReplaced("bande_huse.item_name", regionName).get(0);

            Integer price = BandePlugin.getHouseManager().getPrice(regionName);
            List<Component> lore = Message.getReplaced("bande_huse.item_lore", regionName, price.toString());

            GuiItem item = ItemBuilder.from(mat)
                    .name(name)
                    .lore(lore)
                    .asGuiItem(e -> {
                        BandePlayer player = BandePlugin.getAPI().getPlayer((Player) e.getWhoClicked());
                        if (player.getBande().hasBandeHus()) {
                            Message.sendReplaced(player, "bande_huse.already_owns", null);
                            return;
                        }
                        Integer reqLevel = BandePlugin.getConfiguration().getBandeHusReqLevel();
                        if (reqLevel > player.getBande().getLevel()) {
                            Message.sendReplaced(player, "bande_huse.too_low_level", null, reqLevel.toString());
                            return;
                        }
                        Economy econ = BandePlugin.getEconomy();
                        if (econ.has(player.getBase(), price)) {
                            Message.sendReplaced(player, "general.no_money", null, price.toString());
                            return;
                        }

                        Utils.scheduleClose(e);
                        player.getBande().setBandeHus(regionName);
                        BandePlugin.getHouseManager().rent((Player) e.getWhoClicked(), regionName);
                        Message.sendReplaced(player.getBande().getMemberIterable(), "bande_huse.bought", null,
                                e.getWhoClicked().getName(), regionName);
                    });

            items.add(item);
        }

        return items.toArray(new GuiItem[0]);
    }
}
