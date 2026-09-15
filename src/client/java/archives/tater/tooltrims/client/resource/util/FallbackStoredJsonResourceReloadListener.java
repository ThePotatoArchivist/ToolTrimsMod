package archives.tater.tooltrims.client.resource.util;

import com.mojang.serialization.Codec;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;

import com.google.common.collect.ImmutableMap;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static java.util.Objects.requireNonNull;

public abstract class FallbackStoredJsonResourceReloadListener<T> extends StoredJsonResourceReloadListener<T> {
    private @Nullable CompletableFuture<Map<Identifier, T>> fallbackValues = null;

    protected FallbackStoredJsonResourceReloadListener(Codec<T> codec, FileToIdConverter lister) {
        super(codec, lister);
    }

    protected abstract Map<Identifier, T> getFallbackValues();

    @Override
    public CompletableFuture<Void> reload(SharedState currentReload, Executor taskExecutor, PreparationBarrier preparationBarrier, Executor reloadExecutor) {
        fallbackValues = CompletableFuture.supplyAsync(this::getFallbackValues, taskExecutor);
        return fallbackValues.thenAcceptBoth(super.reload(currentReload, taskExecutor, preparationBarrier, reloadExecutor), (_, _) -> {});
    }

    @Override
    public void apply(CompletableFuture<Map<Identifier, T>> entriesFuture) {
        super.apply(requireNonNull(fallbackValues).thenCombine(entriesFuture, FallbackStoredJsonResourceReloadListener::merge));
    }

    private static <K, V> Map<K, V> merge(Map<K, V> first, Map<K, V> second) {
        var builder = ImmutableMap.<K, V>builder();
        builder.putAll(first);
        builder.putAll(second);
        return builder.buildKeepingLast();
    }
}
