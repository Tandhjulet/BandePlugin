package dk.tandhjulet.api.nbt;

import org.bukkit.inventory.ItemStack;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTItem;

public class NBTAPI {

    public static void init() {
        new NBTContainer("{a:1}").toString();
    }

    public static ItemStack addNBTToItemStack(ItemStack stack, NBTCompound comp) {
        NBTContainer nbt = NBTItem.convertItemtoNBT(stack);
        nbt.mergeCompound(comp);
        ItemStack newItem = NBTItem.convertNBTtoItem(nbt);
        return newItem;
    }

    public static String getTag(String tag, ItemStack item) {
        NBTItem nbti = new NBTItem(item);
        return nbti.getString(tag);
    }

    public static boolean hasKey(String tag, ItemStack item) {
        NBTItem nbti = new NBTItem(item);
        return nbti.hasTag(tag);
    }

    public static ItemStack setTag(String tag, String value, ItemStack item) {
        NBTItem nbti = new NBTItem(item);
        nbti.setString(tag, value);
        return nbti.getItem();
    }

    public static ItemStack removeTag(String tag, ItemStack item) {
        NBTItem nbti = new NBTItem(item);
        nbti.removeKey(tag);
        return nbti.getItem();
    }
}
