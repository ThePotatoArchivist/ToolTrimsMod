package archives.tater.tooltrims.client.resource.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public abstract class PreparationJsonResourceReloadListener<T> implements PreparableReloadListener {
    private final DynamicOps<JsonElement> ops;
    private final Codec<T> codec;
    private final FileToIdConverter lister;

    protected PreparationJsonResourceReloadListener(final HolderLookup.Provider registries, final Codec<T> codec, final ResourceKey<? extends Registry<T>> registryKey) {
        this(registries.createSerializationContext(JsonOps.INSTANCE), codec, FileToIdConverter.registry(registryKey));
    }

    protected PreparationJsonResourceReloadListener(final Codec<T> codec, final FileToIdConverter lister) {
        this(JsonOps.INSTANCE, codec, lister);
    }

    private PreparationJsonResourceReloadListener(final DynamicOps<JsonElement> ops, final Codec<T> codec, final FileToIdConverter lister) {
        this.ops = ops;
        this.codec = codec;
        this.lister = lister;
    }

    @Override
    public CompletableFuture<Void> reload(SharedState currentReload, Executor taskExecutor, PreparationBarrier preparationBarrier, Executor reloadExecutor) {
        var manager = currentReload.resourceManager();
        var entries = CompletableFuture.supplyAsync(() -> {
            Map<Identifier, T> result = new HashMap<>();
            SimpleJsonResourceReloadListener.scanDirectory(manager, lister, ops, codec, result);
            return result;
        }, taskExecutor);
        apply(entries);
        return entries.thenCompose(preparationBarrier::wait).thenRun(() -> {});
    }

    public abstract void apply(CompletableFuture<Map<Identifier, T>> entriesFuture);
}
