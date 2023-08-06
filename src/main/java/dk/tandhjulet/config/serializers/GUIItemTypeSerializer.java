package dk.tandhjulet.config.serializers;

import java.lang.reflect.Type;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTItem;
import dk.tandhjulet.gui.GUIItem;

public class GUIItemTypeSerializer implements TypeSerializer<GUIItem> {

    @Override
    public GUIItem deserialize(Type type, ConfigurationNode node) throws SerializationException {
        NBTContainer nbt = new NBTContainer(node.node("nbt").getString());
        return new GUIItem(NBTItem.convertNBTtoItem(nbt), null);
    }

    @Override
    public void serialize(Type type, @Nullable GUIItem obj, ConfigurationNode node) throws SerializationException {
        node.node("nbt").set(String.class, obj.item.serialize().toString());
    }
}
