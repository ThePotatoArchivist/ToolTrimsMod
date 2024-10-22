package archives.tater.tooltrims.datagen.lib;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public final class FutureCollector implements Collector<CompletableFuture<?>, List<CompletableFuture<?>>, CompletableFuture<?>> {

    private static final Set<Characteristics> CH_UNORDERED_NOID = Collections.unmodifiableSet(EnumSet.of(Characteristics.UNORDERED));

    public static FutureCollector futureCollector() {
        return new FutureCollector();
    }

    @Override
    public Supplier<List<CompletableFuture<?>>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<CompletableFuture<?>>, CompletableFuture<?>> accumulator() {
        return List::add;
    }

    @Override
    public BinaryOperator<List<CompletableFuture<?>>> combiner() {
        return (left, right) -> {
            left.addAll(right);
            return left;
        };
    }

    @Override
    public Function<List<CompletableFuture<?>>, CompletableFuture<?>> finisher() {
        return list -> CompletableFuture.allOf(list.toArray(new CompletableFuture[0]));
    }

    @Override
    public Set<Characteristics> characteristics() {
        return CH_UNORDERED_NOID;
    }
}
