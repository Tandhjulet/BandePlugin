package dk.tandhjulet.gui;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.config.BandeConfig;
import dk.tandhjulet.config.IConfig;
import dk.tandhjulet.config.holder.GUIHolder;
import dk.tandhjulet.events.BandeGUICloseEvent;
import dk.tandhjulet.events.BandeGUIOpenEvent;
import dk.tandhjulet.events.BandeGUIOutsideClickEvent;
import dk.tandhjulet.migrator.Migrate;
import dk.tandhjulet.storage.Message;
import net.kyori.adventure.text.Component;

public class GUI implements IConfig, Serializable {
    @Deprecated
    private static transient final long serialVersionUID = 3L;

    private BandeConfig config;
    private GUIHolder holder;

    private transient GuiAction<InventoryCloseEvent> closeGuiAction;
    private transient GuiAction<InventoryOpenEvent> openGuiAction;
    private transient GuiAction<InventoryClickEvent> outsideClickAction;

    // special use case; used in member administration
    private UUID offender;

    public GUI(String id) {
        this(id, null);
    }

    protected void setOffender(UUID uuid) {
        offender = uuid;
    }

    protected UUID getOffender() {
        return offender;
    }

    public GUI(String id, Integer rows) {
        final File folder = new File(BandePlugin.getPlugin().getDataFolder(), "guis");
        if (!folder.exists() && !folder.mkdirs()) {
            throw new RuntimeException("Unable to create gui folder!");
        }

        config = new BandeConfig(new File(folder, id + ".yml"));
        config.setSaveHook(() -> {
            config.setRootHolder(GUIHolder.class, holder);
        });

        if (holder.id() == null) {
            holder.id(id);
        }
        if (holder.rows() != rows) {
            holder.rows(rows);
        }

        init();
    }

    public String getId() {
        return holder.id();
    }

    public void init() {
        setOutsideClickAction(null);
        setCloseGuiAction(null);
        setOpenGuiAction(null);

        holder.contents().values().stream().forEach(item -> item.loadConsumer());
    }

    public Integer getRows() {
        return holder.rows();
    }

    public void setOutsideClickAction(final GuiAction<InventoryClickEvent> outsideClickAction) {
        this.outsideClickAction = (event) -> {
            if (outsideClickAction != null)
                outsideClickAction.execute(event);

            Bukkit.getPluginManager().callEvent(new BandeGUIOutsideClickEvent(event));
        };
    }

    public void setCloseGuiAction(final GuiAction<InventoryCloseEvent> closeGuiAction) {
        this.closeGuiAction = (event) -> {
            if (closeGuiAction != null)
                closeGuiAction.execute(event);

            Bukkit.getPluginManager().callEvent(new BandeGUICloseEvent(event));
        };
    }

    public void setOpenGuiAction(final GuiAction<InventoryOpenEvent> openGuiAction) {
        this.openGuiAction = (event) -> {
            if (openGuiAction != null)
                openGuiAction.execute(event);

            Bukkit.getPluginManager().callEvent(new BandeGUIOpenEvent(event));
        };
    }

    public void setItem(int id, GUIItem item) {
        holder.contents().put(id, item);
        config.save();
    }

    public void setItem(int id, GuiItem item) {
        holder.contents().put(id, new GUIItem(item.getItemStack(), null));
        config.save();
    }

    public void setContents(HashMap<Integer, GUIItem> contents) {
        holder.contents(contents);
        config.save();
    }

    public void open(Player openTo, OfflinePlayer data, final boolean interaction, final boolean handle) {

        String name = BandePlugin.getInventoryDataHolder().getName(holder.id());
        Integer size = BandePlugin.getInventoryDataHolder().getSize(holder.id());
        if (size == null)
            size = holder.rows();

        BandePlayer bandePlayer = BandePlugin.getAPI().getPlayer(data.getUniqueId());
        final Bande bande;
        if (bandePlayer.getBande() != null) {
            bande = BandePlugin.getAPI().getBande(bandePlayer.getBande().getName());
        } else {
            bande = null;
        }

        Gui gui = Gui.gui().rows(size).title(Component.text(name)).create();

        if (!interaction)
            gui.disableAllInteractions();
        else
            gui.enableAllInteractions();

        holder.contents().forEach((slot, item) -> {

            if (interaction) {

                GuiItem i = item.getItem(data, bande, true);
                i.setAction(null);
                gui.setItem(slot, i);

            } else {

                item.setOffender(offender);
                item.setHandle(handle);
                gui.setItem(slot, item.getItem(data, bande, false));
            }
        });

        gui.setOutsideClickAction(outsideClickAction);
        gui.setOpenGuiAction(openGuiAction);
        if (interaction)
            gui.setCloseGuiAction(closeGuiAction);
        else
            gui.setCloseGuiAction(e -> {
                if (closeGuiAction != null)
                    closeGuiAction.execute(e);

                BandePlayer p = BandePlugin.getAPI().getPlayer(e.getPlayer().getUniqueId());
                p.setPreviousGUI(getId());
            });

        if (!interaction && handle)
            if (getId().equalsIgnoreCase("bande_invites")) {
                GuiItem[] items = DynamicGUI.populateInvites(bandePlayer);

                if (items.length == 0) {
                    gui.setItem(3, 5, ItemBuilder.from(Material.BARRIER)
                            .name(Message.getAsComponent("dynamic_items.no_invites").get(0)).asGuiItem());
                } else {
                    gui.addItem(true, items);
                }
            } else if (getId().equalsIgnoreCase("bande_medlemmer")) {
                gui.addItem(true, DynamicGUI.populateMembers(bande, data.getPlayer()));
            } else if (getId().equalsIgnoreCase("bande_forhold_ally")) {
                GuiItem[] items = DynamicGUI.populateAllys(bande, bandePlayer);

                if (items.length == 0) {
                    gui.setItem(3, 5, ItemBuilder.from(Material.BARRIER)
                            .name(Message.getAsComponent("dynamic_items.no_allys").get(0)).asGuiItem());
                } else {
                    gui.addItem(true, items);
                }

            } else if (getId().equalsIgnoreCase("bande_forhold_rival")) {
                GuiItem[] items = DynamicGUI.populateRivals(bande, bandePlayer);

                if (items.length == 0) {
                    gui.setItem(3, 5, ItemBuilder.from(Material.BARRIER)
                            .name(Message.getAsComponent("dynamic_items.no_rivals").get(0)).asGuiItem());
                } else {
                    gui.addItem(true, items);
                }
            } else if (getId().equalsIgnoreCase("bande_huse")) {
                GuiItem[] items = DynamicGUI.populateHouses(bande, bandePlayer);

                if (items != null && items.length > 0) {
                    gui.addItem(true, items);
                }
            }

        gui.open((HumanEntity) openTo);
    }

    @Override
    public void reloadConfig() {
        config.load();
    }

    @Migrate
    @Deprecated
    private HashMap<Integer, GUIItem> contents;

    @Migrate
    @Deprecated
    private String id;

    @Migrate
    @Deprecated
    private Integer rows;
}
