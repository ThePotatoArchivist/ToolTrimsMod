package archives.tater.tooltrims.client.resource;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.ToolTrimsUtil;
import archives.tater.tooltrims.client.resource.util.FallbackStoredJsonResourceReloadListener;

import net.fabricmc.loader.api.FabricLoader;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.world.item.equipment.trim.TrimMaterial;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import static archives.tater.tooltrims.ToolTrimsUtil.tryList;
import static net.minecraft.util.Util.toMap;

public record ClientTrimMaterial(Identifier assetName, String suffix) {
    public static final Codec<ClientTrimMaterial> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("asset_name").forGetter(ClientTrimMaterial::assetName),
            ToolTrimsUtil.IDENTIFIER_PATH_CODEC.fieldOf("suffix").forGetter(ClientTrimMaterial::suffix)
    ).apply(instance, ClientTrimMaterial::new));

    public static class Loader extends FallbackStoredJsonResourceReloadListener<ClientTrimMaterial> {
        public static final String PATH = "tooltrims/trim_material";
        public static final String TRIM_MATERIAL_PATH = Registries.elementsDirPath(Registries.TRIM_MATERIAL);
        private static final FileToIdConverter LISTER = FileToIdConverter.json(PATH);

        public Loader() {
            super(CODEC, LISTER);
        }

        @Override
        protected Map<Identifier, ClientTrimMaterial> getFallbackValues() {
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
                    .collect(toMap());
        }
    }
}
