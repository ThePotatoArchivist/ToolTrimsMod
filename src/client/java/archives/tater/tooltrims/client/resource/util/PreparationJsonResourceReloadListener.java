package archives.tater.tooltrims.client.resource.util;

import archives.tater.tooltrims.mixin.client.RegistryOpsAccessor;

import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.util.StrictJsonParser;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public abstract class PreparationJsonResourceReloadListener<T> implements PreparableReloadListener {
    private final DynamicOps<JsonElement> ops;
    private final Codec<T> codec;
    private final FileToIdConverter lister;

    private static final Logger LOGGER = LogUtils.getLogger();

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
            var registryInfo = ops instanceof RegistryOpsAccessor accessor ? accessor.getLookupProvider() : null;

            for (var entry : lister.listMatchingResources(manager).entrySet()) {
                var location = entry.getKey();
                var id = lister.fileToId(location);

                try (var reader = entry.getValue().openAsReader()) {
                    var resourceData = StrictJsonParser.parse(reader);

                    if (resourceData.isJsonObject()) {
                        var obj = resourceData.getAsJsonObject();

                        var dataType = this.lister.prefix();

                        if (obj.has(ResourceConditions.CONDITIONS_KEY)) {
                            var conditions = ResourceCondition.CONDITION_CODEC.decode(ops, obj.get(ResourceConditions.CONDITIONS_KEY)).map(com.mojang.datafixers.util.Pair::getFirst);
                            switch (conditions) {
                                case DataResult.Success<ResourceCondition>(var condition, var _) -> {
                                    if (condition.test(registryInfo))
                                        continue;
                                }
                                case DataResult.Error<ResourceCondition> error ->
                                        LOGGER.error("Failed to parse resource conditions for file of type {} with id {}, skipping: {}", dataType, entry.getKey(), error.message());
                            }
                        }
                    }

                    codec.parse(ops, resourceData).ifSuccess(parsed -> {
                        if (result.putIfAbsent(id, parsed) != null)
                            throw new IllegalStateException("Duplicate data file ignored with ID " + id);
                    }).ifError(error ->
                            LOGGER.error("Couldn't parse data file '{}' from '{}': {}", id, location, error)
                    );
                } catch (IllegalArgumentException | IOException | JsonParseException e) {
                    LOGGER.error("Couldn't parse data file '{}' from '{}'", id, location, e);
                }
            }
            return result;
        }, taskExecutor);
        apply(entries);
        return entries.thenCompose(preparationBarrier::wait).thenRun(() -> {});
    }

    public abstract void apply(CompletableFuture<Map<Identifier, T>> entriesFuture);
}
