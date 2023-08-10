package dk.tandhjulet.config.serializers;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.NBTItem;
import dk.tandhjulet.api.HeadDataBaseAPI;
import dk.tandhjulet.gui.GUIItem;
import dk.tandhjulet.utils.ItemSerialization;

public class GUIItemTypeSerializer implements TypeSerializer<GUIItem> {

    @Override
    public GUIItem deserialize(Type type, ConfigurationNode node) throws SerializationException {
        // ItemStack item = ItemSerialization.itemFrom64(node.node("nbt").getString());
        // return new GUIItem(item, null);
        String nbt = node.node("nbt").getString();
        if (nbt != null) {
            ItemStack item = ItemSerialization.itemFrom64(node.node("nbt").getString());
            serialize(type, new GUIItem(item, null), node);
        }

        ItemStack item;

        item = new ItemStack(Material.valueOf(node.node("type").get(String.class)));
        item.setAmount(node.node("amount").get(Integer.class));
        item.setDurability(node.node("durability").get(Short.class));

        if (item.getType() == Material.SKULL || item.getType() == Material.SKULL_ITEM) {
            item = HeadDataBaseAPI.getFromBase(item, node.node("internal").get(String.class));
        }

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(node.node("meta", "display-name").get(String.class));
        meta.setLore(node.node("meta", "lore").getList(String.class));
        meta.addItemFlags(node.node("meta", "itemflags").getList(ItemFlag.class).toArray(new ItemFlag[0]));

        node.node("meta", "enchants").childrenMap().forEach((ench, level) -> {
            meta.addEnchant(Enchantment.getByName((String) ench), level.getInt(), true);
        });

        item.setItemMeta(meta);

        String bandeType = node.node("bande-type").get(String.class);
        if (bandeType != null) {
            NBTItem nbtItem = new NBTItem(item);
            nbtItem.setString("bande-type", bandeType);
            item = nbtItem.getItem();
        }

        return new GUIItem(item, null);
    }

    @Override
    public void serialize(Type type, @Nullable GUIItem obj, ConfigurationNode node) throws SerializationException {
        // node.node("nbt").set(String.class, ItemSerialization.itemTo64(obj.item));
        NBTItem item = new NBTItem(obj.item);

        node.node("type").set(String.class, obj.item.getType().toString());
        node.node("amount").set(Integer.class, obj.item.getAmount());
        node.node("durability").set(Short.class, obj.item.getDurability());

        if (obj.item.getType() == Material.SKULL || obj.item.getType() == Material.SKULL_ITEM) {
            // node.node("internal").set(String.class, )
            try {
                NBTCompoundList compoundList = item.getCompound("SkullOwner").getCompound("Properties")
                        .getCompoundList("textures");
                String value = compoundList.get(0).getString("Value");
                node.node("internal").set(String.class, value);
            } catch (NullPointerException e) {
            }
        }

        if (obj.item.hasItemMeta()) {
            ItemMeta meta = obj.item.getItemMeta();

            node.node("meta", "display-name").set(String.class, meta.getDisplayName());
            node.node("meta", "lore").setList(String.class, meta.getLore());

            if (meta.hasEnchants()) {
                meta.getEnchants().forEach((ench, level) -> {
                    try {
                        node.node("meta", "enchants", ench.toString()).set(Integer.class, level);
                    } catch (SerializationException e) {
                        e.printStackTrace();
                    }
                });
            }
            node.node("meta", "itemflags").setList(ItemFlag.class, new ArrayList<>(meta.getItemFlags()));

        }

        if (item.hasTag("bande-type")) {
            node.node("bande-type").set(String.class, item.getString("bande-type"));
        }

        node.removeChild("nbt");
    }
}
