package mixailche.jsonplaceholder.proxy.core.util;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

@UtilityClass
public class MapUtils {

    public static <K, V, T, U> Map<T, U> bimap(Map<? extends K, ? extends V> map,
                                               Function<? super K, T> keyMapper,
                                               Function<? super V, U> valueMapper) {
        Map<T, U> result = new HashMap<>();
        map.forEach((key, value) -> result.put(
                keyMapper.apply(key),
                valueMapper.apply(value)
                ));
        return result;
    }

    public static <K, V, R> Map<R, V> mapKeys(Map<? extends K, V> map,
                                       Function<? super K, R> function) {
        return bimap(map, function, Function.identity());
    }

    public static <K, V, R> Map<K, R> mapValues(Map<K, ? extends V> map,
                                         Function<? super V, R> function) {
        return bimap(map, Function.identity(), function);
    }

}
