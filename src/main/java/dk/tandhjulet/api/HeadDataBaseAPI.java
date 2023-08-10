package dk.tandhjulet.api;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import dk.tandhjulet.BandePlugin;

public class HeadDataBaseAPI implements Listener {

    public HeadDataBaseAPI() {

    }

    public void registerEvents() {
        BandePlugin.getPlugin().getServer().getPluginManager().registerEvents(this, BandePlugin.getPlugin());
    }

    public static ItemStack getFromBase(String id) {
        return getFromBase(getSkull(), id);
    }

    @SuppressWarnings("deprecation")
    public static ItemStack getFromBase(ItemStack skull, String id) {
        if (id == null || skull == null) {
            return skull;
        }
        UUID uuid = new UUID(id.hashCode(), id.hashCode());

        return Bukkit.getUnsafe().modifyItemStack(skull,
                "{SkullOwner:{Id:\"" + uuid + "\",Properties:{textures:[{Value:\"" + id + "\"}]}}}");
    }

    public static ItemStack getSkull() {
        return new ItemStack(Material.SKULL, 1, (short) 3);
    }

    public static ItemStack getPlayerHead(final String player) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        meta.setOwner(player);

        item.setItemMeta(meta);
        return item;
    }
}