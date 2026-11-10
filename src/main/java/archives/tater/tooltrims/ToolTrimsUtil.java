package archives.tater.tooltrims;

import com.mojang.datafixers.util.Function4;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.Identifier;

import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableMap;

@ApiStatus.NonExtendable
public interface ToolTrimsUtil {

    Codec<String> IDENTIFIER_PATH_CODEC = Codec.STRING.validate(suffix -> Identifier.isValidPath(suffix)
            ? DataResult.success(suffix)
            : DataResult.error(() -> "Non [a-z0-9/._-] character in path for location: " + suffix, suffix)
    );

    static Stream<Path> tryList(Path dir) {
        try {
            return Files.list(dir);
        } catch (IOException e) {
            return Stream.of();
        }
    }

    static <T1, T2, T3, T4, R> CompletableFuture<R> combineAsync(
            CompletableFuture<T1> first,
            CompletableFuture<T2> second,
            CompletableFuture<T3> third,
            CompletableFuture<T4> fourth,
            Function4<T1, T2, T3, T4, R> combine,
            Executor executor
    ) {
        return first.thenCombine(second, Pair::of).thenCombine(third, Pair::of).thenCombineAsync(fourth, (pairPair, t4) -> {
            var t1 = pairPair.getFirst().getFirst();
            var t2 = pairPair.getFirst().getSecond();
            var t3 = pairPair.getSecond();
            return combine.apply(t1, t2, t3, t4);
        }, executor);
    }

    static <K, V> Map<K, V> merge(Map<K, V> base, Map<K, V> apply) {
        return Stream.concat(base.entrySet().stream(), apply.entrySet().stream())
                .collect(toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue, (_, v2) -> v2));
    }
}
