package archives.tater.tooltrims.client.resource.util;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;

import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public abstract class PreparationJsonResourceReloadListener<T> implements PreparableReloadListener {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final Listener jsonResourceReloadListener;

    public PreparationJsonResourceReloadListener(final Codec<T> codec, final FileToIdConverter lister) {
        jsonResourceReloadListener = new Listener(codec, lister);
    }

    @Override
    public CompletableFuture<Void> reload(SharedState currentReload, Executor taskExecutor, PreparationBarrier preparationBarrier, Executor reloadExecutor) {
        var manager = currentReload.resourceManager();
        var entries = CompletableFuture.supplyAsync(() -> jsonResourceReloadListener.prepare(manager, Profiler.get()), taskExecutor);
        apply(entries);
        return entries.thenCompose(preparationBarrier::wait).thenRun(() -> {});
    }

    public abstract void apply(CompletableFuture<Map<Identifier, T>> entriesFuture);

    private class Listener extends SimpleJsonResourceReloadListener<T> {
        protected Listener(Codec<T> codec, FileToIdConverter lister) {
            super(codec, lister);
        }

        @Override
        public Map<Identifier, T> prepare(ResourceManager manager, ProfilerFiller profiler) {
            return super.prepare(manager, profiler);
        }

        @Override
        protected void apply(Map<Identifier, T> preparations, ResourceManager manager, ProfilerFiller profiler) {

        }
    }
}
