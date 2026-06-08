package archives.tater.tooltrims.client.data.util;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class StoredJsonResourceReloadListener<T> extends PreparationJsonResourceReloadListener<T> {
    private CompletableFuture<Map<Identifier, T>> entries = CompletableFuture.completedFuture(Map.of());

    protected StoredJsonResourceReloadListener(HolderLookup.Provider registries, Codec<T> codec, ResourceKey<? extends Registry<T>> registryKey) {
        super(registries, codec, registryKey);
    }

    protected StoredJsonResourceReloadListener(Codec<T> codec, FileToIdConverter lister) {
        super(codec, lister);
    }

    @Override
    public void apply(CompletableFuture<Map<Identifier, T>> entriesFuture) {
        this.entries = entriesFuture;
    }

    public CompletableFuture<Map<Identifier, T>> entries() {
        return entries;
    }
}
