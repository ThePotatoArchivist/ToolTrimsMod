package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrims;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StrictJsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static net.minecraft.util.Util.throwAsRuntime;

public class LanguageMergeGenerator extends FabricCodecDataProvider<Map<String, String>> {
    private static final Codec<Map<String, String>> CODEC = Codec.unboundedMap(Codec.STRING, Codec.STRING);
    private static final ModContainer MOD_CONTAINER = FabricLoader.getInstance().getModContainer(ToolTrims.MOD_ID).orElseThrow();

    private static final Map<String, List<String>> LANGUAGE_CODE_COPIES = Map.of(
            "de_de", List.of(),
            "en_us", List.of(),
            "es_es", List.of("es_ar", "es_mx", "es_cl", "es_ec", "es_uy", "es_ve"),
            "fr_fr", List.of("fr_ca"),
            "ja_jp", List.of(),
            "pl_pl", List.of(),
            "pt_pt", List.of("pt_br"),
            "ru_ru", List.of(),
            "uk_ua", List.of()
    );

    protected LanguageMergeGenerator(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture, PackOutput.Target.RESOURCE_PACK, "lang", CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, Map<String, String>> provider, HolderLookup.Provider registryLookup) {
        LANGUAGE_CODE_COPIES.forEach((source, copies) -> {
            Map<String, String> translations = new HashMap<>();

            merge(translations, findLangPath("src/" + source));
            merge(translations, findLangPath("patch/" + source));

            provider.accept(ToolTrims.id(source), translations);
            for (var code : copies)
                provider.accept(ToolTrims.id(code), translations);
        });
    }

    private Path findLangPath(String path) {
        return LanguageMergeGenerator.MOD_CONTAINER.findPath("assets/" + ToolTrims.MOD_ID + "/lang/" + path + ".json").orElseThrow();
    }

    private static void merge(Map<String, String> map, Path path) {
        try (var reader = Files.newBufferedReader(path)) {
            var translations = StrictJsonParser.parse(reader).getAsJsonObject();

            for (var key : translations.keySet())
                map.put(key, translations.get(key).getAsString());
        } catch (IOException e) {
            throwAsRuntime(e);
        }
    }

    @Override
    public String getName() {
        return "Language Merge";
    }
}
