package astral_mekanism.util;

import java.util.EnumMap;
import java.util.function.Function;

public class AMEUtils {
    public static <T extends Enum<T>, V> EnumMap<T, V> createFilledMap(Class<T> clazz, Function<T, V> filling) {
        EnumMap<T, V> result = new EnumMap<>(clazz);
        for (T t : clazz.getEnumConstants()) {
            result.put(t, filling.apply(t));
        }
        return result;
    }
}
