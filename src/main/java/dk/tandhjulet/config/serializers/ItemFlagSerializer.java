package dk.tandhjulet.config.serializers;

import java.lang.reflect.Type;
import java.util.function.Predicate;

import org.bukkit.inventory.ItemFlag;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

public class ItemFlagSerializer extends ScalarSerializer<ItemFlag> {

    public ItemFlagSerializer() {
        super(ItemFlag.class);
    }

    @Override
    public ItemFlag deserialize(Type type, Object obj) throws SerializationException {
        return ItemFlag.valueOf((String) obj);
    }

    @Override
    protected Object serialize(ItemFlag flag, Predicate<Class<?>> typeSupported) {
        return flag.name();
    }

}
