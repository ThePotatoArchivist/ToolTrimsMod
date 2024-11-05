package archives.tater.tooltrims;

import org.apache.commons.lang3.stream.Streams;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ToolTrimsUtil {
    private ToolTrimsUtil() {}

    public static <K, V> Map<K, V> associateWith(Iterable<K> items, Function<K, V> valueMapper) {
        return Streams.of(items.iterator()).collect(Collectors.toMap(Function.identity(), valueMapper));
    }
}
