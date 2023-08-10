package dk.tandhjulet.gui;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.GuiItem;
import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.api.nbt.NBTAPI;
import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.commands.subcommands.Demote;
import dk.tandhjulet.commands.subcommands.Kick;
import dk.tandhjulet.commands.subcommands.Promote;
import dk.tandhjulet.enums.BandeRank;
import dk.tandhjulet.level.Level;
import dk.tandhjulet.placeholders.BandePlaceholders;
import dk.tandhjulet.storage.Message;
import dk.tandhjulet.utils.Logger;
import dk.tandhjulet.utils.Utils;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;

public class GUIItem implements Serializable {
    @Deprecated
    private static transient final long serialVersionUID = 4L;

    public ItemStack item;
    transient GuiAction<InventoryClickEvent> consumer;
    transient String type;

    // special use case; used in member administration
    transient private UUID offender;
    transient private boolean handle;

    public GUIItem(ItemStack item, GuiAction<InventoryClickEvent> consumer) {
        this.item = item;
        this.consumer = consumer;
        loadConsumer();
    }

    protected void setOffender(UUID uuid) {
        this.offender = uuid;
    }

    protected void setHandle(boolean handle) {
        this.handle = handle;
    }

    public void loadConsumer() {
        this.type = NBTAPI.getTag("bande-type", item);
        if (type != null && NBTAPI.hasKey("bande-type", item)) {
            this.consumer = BandePlugin.getTypeManager().get(type);
        }
    }

    public GuiItem getItem() {
        return getItem(null, null, false);
    }

    public GuiItem getItem(final Player player) {
        return getItem(player, null, false);
    }

    public GuiItem getItem(final OfflinePlayer player, final Bande bande, final boolean interaction) {
        ItemStack newItem = item.clone();

        if (!interaction)
            if (NBTAPI.hasKey("player-skull", newItem)) {
                newItem = ItemBuilder.skull(newItem).owner(player).build();
            }

        if (!interaction) {
            String tag = NBTAPI.getTag("bande-type", newItem);
            if (tag.equalsIgnoreCase("bande-ally-item") && bande != null) {
                if (bande.getAllianceAmount() > 0) {
                    String prefix = BandePlugin.getConfiguration().getAllyListFormat();

                    List<Component> allies = bande.getAlliances().stream().map(e -> {
                        return Component.text(Utils.getColored(prefix + e));
                    }).collect(Collectors.toList());

                    newItem = ItemBuilder.from(newItem).lore(allies).build();
                } else {
                    newItem = ItemBuilder.from(newItem)
                            .lore(Component.text(Utils.getColored(BandePlugin.getConfiguration().getNoAllyMessage())))
                            .build();
                }
            } else if (tag.equalsIgnoreCase("bande-rival-item") && bande != null) {
                if (bande.getRivalAmount() > 0) {
                    String prefix = BandePlugin.getConfiguration().getRivalListFormat();

                    List<Component> rivals = bande.getRivals().stream().map(e -> {
                        return Component.text(Utils.getColored(prefix + e));
                    }).collect(Collectors.toList());

                    newItem = ItemBuilder.from(newItem).lore(rivals).build();
                } else {
                    newItem = ItemBuilder.from(newItem)
                            .lore(Component.text(Utils.getColored(BandePlugin.getConfiguration().getNoRivalMessage())))
                            .build();
                }
            } else if (tag.equalsIgnoreCase("bande-top-offikills-item")
                    || tag.equalsIgnoreCase("bande-top-vagtkills-item")
                    || tag.equalsIgnoreCase("bande-top-level-item")
                    || tag.equalsIgnoreCase("bande-top-fangekills-item")) {

                Map<String, Integer> map = new LinkedHashMap<>();
                if (tag.equalsIgnoreCase("bande-top-offikills-item"))
                    map = BandePlugin.getTop().getSortedOffiKills();
                else if (tag.equalsIgnoreCase("bande-top-vagtkills-item"))
                    map = BandePlugin.getTop().getSortedVagtKills();
                else if (tag.equalsIgnoreCase("bande-top-level-item"))
                    map = BandePlugin.getTop().getSortedLevels();
                else if (tag.equalsIgnoreCase("bande-top-fangekills-item"))
                    map = BandePlugin.getTop().getSortedFangeKills();

                List<Component> top = new LinkedList<>();
                AtomicInteger index = new AtomicInteger(0);

                for (Entry<String, Integer> entry : map.entrySet()) {
                    if (index.incrementAndGet() == 10) {
                        break;
                    }

                    top.add(Message.getReplaced("dynamic_items.top_list_format",
                            String.valueOf(index.get()), entry.getKey(),
                            entry.getValue().toString()).get(0));
                }

                for (Integer i = top.size() + 1; i <= 10; i++) {

                    top.add(Message.getReplaced("dynamic_items.top_list_format",
                            String.valueOf(i), "N/A",
                            "0").get(0));
                }

                newItem = ItemBuilder.from(newItem).lore(top).build();
            } else if (tag.equalsIgnoreCase("bande-member-promote-item")) {
                consumer = e -> {
                    if (offender == null)
                        return;

                    BandePlayer p = BandePlugin.getAPI().getPlayer(offender);
                    if (p == null) {
                        Logger.warn("Wrong use of bande-promote-item detected!");
                        return;
                    }

                    BandePlayer sender = BandePlugin.getAPI().getPlayer((Player) e.getWhoClicked());

                    Promote.run(sender, new String[] { "", p.getBase().getName() });
                    Utils.scheduleClose(e);
                };
            } else if (tag.equalsIgnoreCase("bande-member-kick-item")) {
                consumer = e -> {
                    if (offender == null)
                        return;

                    BandePlayer p = BandePlugin.getAPI().getPlayer(offender);
                    if (p == null) {
                        Logger.warn("Wrong use of bande-promote-item detected!");
                        return;
                    }

                    BandePlayer sender = BandePlugin.getAPI().getPlayer((Player) e.getWhoClicked());

                    Kick.run(sender, new String[] { "", p.getBase().getName() });
                    Utils.scheduleClose(e);
                };
            } else if (tag.equalsIgnoreCase("bande-member-demote-item")) {
                consumer = e -> {
                    if (offender == null)
                        return;

                    BandePlayer p = BandePlugin.getAPI().getPlayer(offender);
                    if (p == null) {
                        Logger.warn("Wrong use of bande-promote-item detected!");
                        return;
                    }

                    BandePlayer sender = BandePlugin.getAPI().getPlayer((Player) e.getWhoClicked());

                    Demote.run(sender, new String[] { "", p.getBase().getName() });
                    Utils.scheduleClose(e);
                };
            } else if (tag.equalsIgnoreCase("bande-begraensninger-item") && bande != null) {
                List<Component> replacedLore = Message.getPlaceholderReplaced(bande, player,
                        "dynamic_items.begraensninger_lore");

                newItem = ItemBuilder.from(newItem).lore(replacedLore).build();
            } else if (tag.equalsIgnoreCase("bande-statistikker") && bande != null) {
                List<Component> replacedLore = Message.getPlaceholderReplaced(bande, player,
                        "dynamic_items.bande_stats_lore");

                newItem = ItemBuilder.from(newItem).lore(replacedLore).build();
            } else if (tag.equalsIgnoreCase("bande-medlemmer-menu")) {
                BandePlayer p = BandePlugin.getAPI().getPlayer(player.getUniqueId());

                List<Component> list = new LinkedList<>();

                if (p.hasBande()) {
                    for (Map.Entry<BandeRank, HashSet<UUID>> entry : p.getBande().getMembers().entrySet()) {
                        for (UUID uuid : entry.getValue()) {
                            OfflinePlayer pl = Bukkit.getOfflinePlayer(uuid);
                            if (pl == null)
                                continue;

                            BandePlayer bandePlayer = BandePlugin.getAPI().getPlayer(pl.getUniqueId());

                            list.addAll(Message.getReplaced("dynamic_items.member_list_format",
                                    bandePlayer.getBandeRank().toString(), bandePlayer.getBase().getName()));
                        }
                    }
                }

                newItem = ItemBuilder.from(newItem).lore(list).build();
            } else if (tag.equalsIgnoreCase("bande-level") && bande != null) {
                Level level = BandePlugin.getLevelManager().get(bande.getLevel() + 1);
                if (level == null) {
                    List<Component> c = Message.getAsComponent("level.max_level");
                    if (c == null) {
                        c = new LinkedList<Component>() {
                            {
                                add(Component.text(Utils.getColored("level.max_level")));
                            }
                        };
                    }

                    newItem = ItemBuilder.from(newItem).lore(c).build();
                } else {
                    newItem = ItemBuilder.from(newItem).lore(level.apply(player, bande)).build();
                }
            } else if (tag.equalsIgnoreCase("bande-hus-item") && bande != null) {
                List<Component> c = Message.getPlaceholderReplaced(bande, player, "bande_huse.display_item_lore");

                newItem = ItemBuilder.from(newItem)
                        .lore(c)
                        .build();
            }
        }

        if (!interaction)
            newItem = setPlaceholders(newItem, player, bande);

        if (!handle) {
            return ItemBuilder.from(newItem).asGuiItem();
        }
        return ItemBuilder.from(newItem).asGuiItem(consumer);
    }

    private ItemStack setPlaceholders(final ItemStack item, final OfflinePlayer player, final Bande bande) {
        if (!item.hasItemMeta()) {
            return item;
        }
        ItemMeta meta = item.getItemMeta();

        if (meta.hasDisplayName()) {
            String name = meta.getDisplayName();
            if (player != null && BandePlugin.isPAPIEnabled()) {
                name = PlaceholderAPI.setPlaceholders(player, name);
            }
            if (bande != null && BandePlugin.isPAPIEnabled()) {
                name = BandePlaceholders.setPlaceholders(bande, new LinkedList<String>() {
                    {
                        add(meta.getDisplayName());
                    }
                }).get(0);
            }
            meta.setDisplayName(name);
        }

        if (meta.hasLore()) {
            List<String> lore = meta.getLore();
            if (player != null && BandePlugin.isPAPIEnabled()) {
                lore = PlaceholderAPI.setPlaceholders(player, lore);
            }
            if (bande != null) {
                lore = BandePlaceholders.setPlaceholders(bande, lore);
            }
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        return item;
    }
}
