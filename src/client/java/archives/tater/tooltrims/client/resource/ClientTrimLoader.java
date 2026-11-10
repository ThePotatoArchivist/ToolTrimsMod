package archives.tater.tooltrims.client.resource;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.client.item.UnbakedTrimsModel;

import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.fabricmc.fabric.api.tag.client.v1.ClientTags;
import net.fabricmc.loader.api.FabricLoader;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.renderer.item.*;
import net.minecraft.client.resources.model.cuboid.CuboidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.util.Util;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.equipment.trim.TrimMaterial;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.UnknownNullability;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Stream;

import static archives.tater.tooltrims.ToolTrimsUtil.*;
import static java.util.stream.Collectors.toMap;
import static net.minecraft.client.data.models.model.ItemModelUtils.composite;

public class ClientTrimLoader implements PreparableModelLoadingPlugin.DataLoader<ClientTrimLoader.Result> {

    public static final Identifier FALLBACK_SWORD = ToolTrims.id("fallback_sword");
    public static final Identifier FALLBACK_PICKAXE = ToolTrims.id("fallback_pickaxe");
    public static final Identifier FALLBACK_AXE = ToolTrims.id("fallback_axe");
    public static final Identifier FALLBACK_SHOVEL = ToolTrims.id("fallback_shovel");
    public static final Identifier FALLBACK_HOE = ToolTrims.id("fallback_hoe");
    public static final Identifier FALLBACK_SPEAR = ToolTrims.id("fallback_spear");
    public static final String TRIM_MATERIAL_PATH = Registries.elementsDirPath(Registries.TRIM_MATERIAL);
    private static final Map<Identifier, Identifier> FALLBACKS = Stream.of(
            Pair.of(FALLBACK_SPEAR, ItemTags.SPEARS),
            Pair.of(FALLBACK_AXE, ItemTags.AXES),
            Pair.of(FALLBACK_PICKAXE, ItemTags.PICKAXES),
            Pair.of(FALLBACK_HOE, ItemTags.HOES),
            Pair.of(FALLBACK_SHOVEL, ItemTags.SHOVELS),
            Pair.of(FALLBACK_SWORD, ItemTags.SWORDS)
    )
            .flatMap(entry -> ClientTags.getOrCreateLocalTag(entry.getSecond()).stream()
                    .map(item -> Pair.of(item, entry.getFirst())))
            .collect(toMap(Pair::getFirst, Pair::getSecond, (first, _) -> first));

    private static final Loader<ClientTrimMaterial> MATERIAL_LOADER = new Loader<>(ClientTrimMaterial.CODEC, FileToIdConverter.json(ClientTrimMaterial.PATH));
    private static final Loader<ClientTrimPattern> PATTERN_LOADER = new Loader<>(ClientTrimPattern.CODEC, FileToIdConverter.json(ClientTrimPattern.PATH));
    private static final Loader<ClientTrimOverlay> OVERLAY_LOADER = new Loader<>(ClientTrimOverlay.CODEC, FileToIdConverter.json(ClientTrimOverlay.PATH));

    private static CompletableFuture<Result> pendingResult = CompletableFuture.failedFuture(new IllegalStateException("Called too early"));

    public static CompletableFuture<Result> getPendingResult() {
        return pendingResult;
    }

    private static Map<Identifier, ItemModel.Unbaked> buildOverlays(Map<Identifier, ClientTrimOverlay> entries) {
        var overlays = new ImmutableMap.Builder<Identifier, ItemModel.Unbaked>();

        FALLBACKS.forEach((item, overlayId) -> {
            var overlay = entries.get(overlayId);
            if (overlay == null) return;
            overlays.put(item, overlay.model());
        });

        for (var overlay : entries.values())
            for (var item : overlay.items())
                overlays.put(item, overlay.model());

        return overlays.buildKeepingLast();
    }

    private static List<UnbakedTrimsModel> buildModels(Map<Identifier, ClientTrimOverlay> entries) {
        return entries.values().stream()
                .flatMap(overlay -> extractTrimModels(overlay.model()))
                .distinct()
                .toList();
    }

    private static Stream<UnbakedTrimsModel> extractTrimModels(ItemModel.@Nullable Unbaked model) {
        return switch (model) {
            case UnbakedTrimsModel unbaked -> Stream.of(unbaked);
            case CompositeModel.Unbaked unbaked -> unbaked.models().stream().flatMap(ClientTrimLoader::extractTrimModels);
            case ConditionalItemModel.Unbaked unbaked -> Stream.concat(extractTrimModels(unbaked.onFalse()), extractTrimModels(unbaked.onTrue()));
            case SelectItemModel.Unbaked unbaked -> Stream.concat(extractTrimModels(unbaked.fallback().orElse(null)), unbaked.unbakedSwitch().cases().stream().flatMap(switchCase -> extractTrimModels(switchCase.model())));
            case RangeSelectItemModel.Unbaked unbaked -> Stream.concat(extractTrimModels(unbaked.fallback().orElse(null)), unbaked.entries().stream().flatMap(entry -> extractTrimModels(entry.model())));
            case null, default -> Stream.empty();
        };
    }

    private static Map<Identifier, ClientTrimMaterial> getFallbackMaterials() {
        return FabricLoader.getInstance().getAllMods().stream()
                .flatMap(mod -> mod.getRootPaths().stream())
                .flatMap(path -> tryList(path.resolve("data")))
                .flatMap(nsPath -> tryList(nsPath.resolve(TRIM_MATERIAL_PATH))
                        .filter(path -> path.getFileName().toString().endsWith(".json"))
                        .<Map.Entry<Identifier, ClientTrimMaterial>>mapMulti((path, yield) -> {
                            var filename = path.getFileName().toString();
                            var id = Identifier.fromNamespaceAndPath(nsPath.getFileName().toString(), filename.substring(0, filename.length() - 5));
                            try (var reader = Files.newBufferedReader(path)) {
                                switch (TrimMaterial.DIRECT_CODEC.parse(JsonOps.INSTANCE, StrictJsonParser.parse(reader))) {
                                    case DataResult.Success<TrimMaterial>(TrimMaterial(var paletteId, var _), var _) -> yield.accept(Map.entry(id, new ClientTrimMaterial(paletteId, id.getNamespace() + "_" + id.getPath())));
                                    case DataResult.Error<?> error -> ToolTrims.LOGGER.error("Error loading trim material {}: {}", id, error.message());
                                }
                            } catch (IOException e) {
                                ToolTrims.LOGGER.error("Error loading trim material {}", id, e);
                            }
                        })
                )
                .collect(Util.toMap());
    }

    private static Stream<Pair<Identifier, CuboidModel>> getTrimModels(List<UnbakedTrimsModel> overlays, Collection<ClientTrimPattern> patterns, @UnknownNullability Collection<ClientTrimMaterial> materials) {
        return overlays.stream().flatMap(model ->
                patterns.stream().flatMap(pattern ->
                        materials.stream().map(material -> {
                                var modelId = UnbakedTrimsModel.createModelId(model.basePath(), pattern, material);
                                return Pair.of(modelId, new CuboidModel(
                                        null,
                                        null,
                                        null,
                                        null,
                                        new TextureSlots.Data.Builder().addTexture(model.textureSlot(), new Material(modelId)).build(),
                                        model.parent()
                                ));
                            }
                        )
                )
        );
    }

    @Override
    public CompletableFuture<Result> load(PreparableReloadListener.SharedState resourceReloaderStore, Executor executor) {
        var fallbackMaterialsFuture = CompletableFuture.supplyAsync(ClientTrimLoader::getFallbackMaterials, executor);
        var materialsFuture = MATERIAL_LOADER.load(resourceReloaderStore.resourceManager(), executor);
        var patternsFuture = PATTERN_LOADER.load(resourceReloaderStore.resourceManager(), executor);
        var overlaysFuture = OVERLAY_LOADER.load(resourceReloaderStore.resourceManager(), executor);

        var resultFuture = combineAsync(fallbackMaterialsFuture, materialsFuture, patternsFuture, overlaysFuture, (fallbackMaterials, materials, patterns, overlays) -> {
            var finalMaterials = merge(fallbackMaterials, materials);
            var trimModels = buildModels(overlays);
            return new Result(
                    finalMaterials,
                    patterns,
                    trimModels,
                    getTrimModels(trimModels, patterns.values(), finalMaterials.values()),
                    buildOverlays(overlays)
            );
        }, executor);
        pendingResult = resultFuture;

        return resultFuture;
    }

    public record Result(
            Map<Identifier, ClientTrimMaterial> materials,
            Map<Identifier, ClientTrimPattern> patterns,
            List<UnbakedTrimsModel> trimModels,
            Stream<Pair<Identifier, CuboidModel>> models,
            Map<Identifier, ItemModel.Unbaked> overlays
    ) {
        public ClientItem modifyModel(ClientItem model, Identifier itemId) {
            var trimModel = overlays.get(itemId);
            if (trimModel == null || trimModel instanceof EmptyModel.Unbaked) return model;
            return new ClientItem(
                    composite(
                            model.model(),
                            trimModel
                    ),
                    model.properties(),
                    model.registrySwapper()
            );
        }
    }

    private static class Loader<T> extends SimpleJsonResourceReloadListener<T> {
        protected Loader(Codec<T> codec, FileToIdConverter lister) {
            super(codec, lister);
        }

        private CompletableFuture<Map<Identifier, T>> load(ResourceManager resourceManager, Executor executor) {
            return CompletableFuture.supplyAsync(() -> prepare(resourceManager, Profiler.get()), executor);
        }

        @Deprecated
        @Override
        protected void apply(Map<Identifier, T> preparations, ResourceManager manager, ProfilerFiller profiler) {

        }
    }
}
