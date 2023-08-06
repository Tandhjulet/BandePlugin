package dk.tandhjulet.config.serializers;

import java.lang.reflect.Type;

import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTItem;

public class ItemStackTypeSerializer implements TypeSerializer<ItemStack> {

    @Override
    public ItemStack deserialize(Type type, ConfigurationNode node) throws SerializationException {
        NBTContainer nbt = new NBTContainer(node.node("nbt").getString());
        return NBTItem.convertNBTtoItem(nbt);
    }

    @Override
    public void serialize(Type type, @Nullable ItemStack obj, ConfigurationNode node) throws SerializationException {
        node.node("nbt").set(String.class, obj.serialize().toString());
    }
}
