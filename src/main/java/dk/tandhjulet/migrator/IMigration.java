package dk.tandhjulet.migrator;

import java.io.File;
import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface IMigration {
    public default Map.Entry<List<File>, List<Field>> migrate() {
        List<Field> fields = getFields();
        List<File> files = getFiles();

        return new AbstractMap.SimpleEntry<List<File>, List<Field>>(files, fields);
    }

    public default List<Field> getFields() {
        Class<?> clazz = getClazz();
        List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
        fields.removeIf(field -> !field.isAnnotationPresent(Migrate.class));
        return fields;
    }

    public List<File> getFiles();

    public Class<?> getClazz();
}
