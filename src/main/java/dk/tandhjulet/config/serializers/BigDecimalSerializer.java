package dk.tandhjulet.config.serializers;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.Predicate;

import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

public class BigDecimalSerializer extends ScalarSerializer<BigDecimal> {

    public BigDecimalSerializer() {
        super(BigDecimal.class);
    }

    @Override
    public BigDecimal deserialize(Type type, Object obj) throws SerializationException {
        if (obj instanceof Double)
            return BigDecimal.valueOf((double) obj);

        else if (obj instanceof Integer)
            return BigDecimal.valueOf((int) obj);

        else if (obj instanceof Long)
            return BigDecimal.valueOf((long) obj);

        else if (obj instanceof String)
            return new BigDecimal((String) obj, MathContext.DECIMAL128);
        return null;
    }

    @Override
    public Object serialize(BigDecimal item, Predicate<Class<?>> typeSupported) {
        return item.toString();
    }

}
