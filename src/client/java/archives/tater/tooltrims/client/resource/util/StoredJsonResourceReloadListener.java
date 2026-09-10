package archives.tater.tooltrims.client.resource.util;

import com.mojang.serialization.Codec;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class StoredJsonResourceReloadListener<T> extends PreparationJsonResourceReloadListener<T> {
    private CompletableFuture<Map<Identifier, T>> entries = CompletableFuture.completedFuture(Map.of());

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

    public Map<Identifier, T> joinEntries() {
        return entries().join();
    }
}
