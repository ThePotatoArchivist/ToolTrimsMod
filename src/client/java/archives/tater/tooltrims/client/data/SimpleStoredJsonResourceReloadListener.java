package archives.tater.tooltrims.client.data;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class SimpleStoredJsonResourceReloadListener<T> extends SimpleJsonResourceReloadListener<T> {
    private Map<Identifier, T> entries = Map.of();

    protected SimpleStoredJsonResourceReloadListener(HolderLookup.Provider registries, Codec<T> codec, ResourceKey<? extends Registry<T>> registryKey) {
        super(registries, codec, registryKey);
    }

    protected SimpleStoredJsonResourceReloadListener(Codec<T> codec, FileToIdConverter lister) {
        super(codec, lister);
    }

    @Override
    protected void apply(Map<Identifier, T> preparations, ResourceManager manager, ProfilerFiller profiler) {
        entries = preparations;
    }

    public Map<Identifier, T> entries() {
        return entries;
    }
}
