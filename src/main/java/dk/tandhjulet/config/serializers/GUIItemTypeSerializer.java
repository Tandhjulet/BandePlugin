package dk.tandhjulet.config.serializers;

import java.lang.reflect.Type;

import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import dk.tandhjulet.gui.GUIItem;
import dk.tandhjulet.utils.ItemSerialization;

public class GUIItemTypeSerializer implements TypeSerializer<GUIItem> {

    @Override
    public GUIItem deserialize(Type type, ConfigurationNode node) throws SerializationException {
        ItemStack item = ItemSerialization.itemFrom64(node.node("nbt").getString());
        return new GUIItem(item, null);
    }

    @Override
    public void serialize(Type type, @Nullable GUIItem obj, ConfigurationNode node) throws SerializationException {
        node.node("nbt").set(String.class, ItemSerialization.itemTo64(obj.item));
    }
}
