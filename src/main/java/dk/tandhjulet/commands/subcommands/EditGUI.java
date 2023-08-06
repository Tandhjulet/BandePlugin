package dk.tandhjulet.commands.subcommands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.api.nbt.NBTAPI;
import dk.tandhjulet.gui.GUI;
import dk.tandhjulet.gui.GUIItem;
import dk.tandhjulet.utils.Utils;
import net.kyori.adventure.text.Component;

public class EditGUI {

    @SuppressWarnings("unchecked")
    public static void run(Player player) {
        if (!player.isOp()) {
            return;
        }

        HashMap<String, GUI> guis = (HashMap<String, GUI>) BandePlugin.getGuiManager().getGUIs().clone();

        GUI gui = new GUI("Edit GUIs", 6, true);

        AtomicInteger index = new AtomicInteger(-1);
        guis.forEach((name, GUI) -> {

            GUI.setCloseGuiAction(e -> {
                HashMap<Integer, GUIItem> contents = new HashMap<>();

                AtomicInteger itemIndex = new AtomicInteger(0);
                Arrays.stream(e.getInventory().getContents()).forEach(item -> {
                    if (item != null && item.getType() != Material.AIR) {
                        String tagType = NBTAPI.getTag("bande-type", item);

                        // ItemStack serializableItemStack = NBTAPI.removeTag("bande-type", item);

                        GUIItem guiitem = new GUIItem(item, BandePlugin.getTypeManager().get(tagType));
                        guiitem.loadConsumer();

                        contents.put(itemIndex.get(), guiitem);
                    }
                    itemIndex.incrementAndGet();
                });

                GUI newGUI = new GUI(name, GUI.getRows());
                newGUI.setContents(contents);

                BandePlugin.getGuiManager().removeGui(name);
                BandePlugin.getGuiManager().addGui(name, newGUI);

            });

            ItemStack item = ItemBuilder.from(Material.IRON_DOOR)
                    .name(Component.text(Utils.getColored("&a&l" + name.toUpperCase())))
                    .lore(Component.text(ChatColor.DARK_GREEN + "Klik for at redigere!"))
                    .build();

            GUIItem guiitem = new GUIItem(item, e -> {
                GUI.open(player, player, true, true);
            });

            gui.setItem(index.incrementAndGet(), guiitem);
        });

        gui.open(player, player, false, true);
    }

}
