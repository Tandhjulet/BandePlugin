package dk.tandhjulet.config.serializers;

import java.lang.reflect.Type;
import java.util.UUID;
import java.util.function.Predicate;

import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

public class UUIDSerializer extends ScalarSerializer<UUID> {

    public UUIDSerializer() {
        super(UUID.class);
    }

    @Override
    public UUID deserialize(Type type, Object obj) throws SerializationException {
        return UUID.fromString((String) obj);
    }

    @Override
    public Object serialize(UUID item, Predicate<Class<?>> typeSupported) {
        return item.toString();
    }

}
