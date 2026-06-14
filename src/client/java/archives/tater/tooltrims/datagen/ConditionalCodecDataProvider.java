package archives.tater.tooltrims.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class ConditionalCodecDataProvider<T> extends FabricCodecDataProvider<Pair<T, Optional<ResourceCondition>>> {

    protected ConditionalCodecDataProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture, PackOutput.Target target, String directoryName, Codec<T> codec) {
        super(packOutput, registriesFuture, target, directoryName, Codec.pair(codec, ResourceCondition.CONDITION_CODEC.optionalFieldOf(ResourceConditions.CONDITIONS_KEY).codec()));
    }

    private @Nullable ResourceCondition condition = null; // this is fine

    @Override
    protected void configure(BiConsumer<Identifier, Pair<T, Optional<ResourceCondition>>> provider, HolderLookup.Provider registryLookup) {
        configureC((identifier, t) -> {
            provider.accept(identifier, Pair.of(t, Optional.ofNullable(condition)));
        }, registryLookup);
    }

    protected abstract void configureC(BiConsumer<Identifier, T> provider, HolderLookup.Provider registryLookup);

    protected BiConsumer<Identifier, T> withConditions(BiConsumer<Identifier, T> provider, ResourceCondition condition) {
        return (identifier, t) -> {
            this.condition = condition;
            provider.accept(identifier, t);
        };
    }
}
