package dk.tandhjulet.config.serializers;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Predicate;

import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

import dk.tandhjulet.gui.GUIItem;

public class GUIItemSerializer extends ScalarSerializer<GUIItem> {

    public GUIItemSerializer() {
        super(GUIItem.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public GUIItem deserialize(Type type, Object obj) throws SerializationException {
        ItemStack itemstack = ItemStack.deserialize((Map<String, Object>) obj);
        return new GUIItem(itemstack, null);
    }

    @Override
    public Object serialize(GUIItem item, Predicate<Class<?>> typeSupported) {
        return item.item.serialize();
    }

}
