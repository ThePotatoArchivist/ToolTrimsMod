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
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static net.minecraft.util.Util.throwAsRuntime;

public class LanguageMergeGenerator extends FabricCodecDataProvider<Map<String, String>> {
    private static final Codec<Map<String, String>> CODEC = Codec.unboundedMap(Codec.STRING, Codec.STRING);

    private static final ModContainer MOD_CONTAINER = FabricLoader.getInstance().getModContainer(ToolTrims.MOD_ID).orElseThrow();
    private static final Path DATAPACK_LANG = Path.of(System.getProperty("tooltrims.datapack-lang"));

    protected LanguageMergeGenerator(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture, PackOutput.Target.RESOURCE_PACK, "lang", CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, Map<String, String>> provider, HolderLookup.Provider registryLookup) {
        mergeLang(provider, "de_de");
        mergeLang(provider, "en_us");
        mergeLang(provider, "es_es", "es_ar", "es_mx", "es_cl", "es_ec", "es_uy", "es_ve");
        mergeLang(provider, "fr_fr", "fr_ca");
        mergeLang(provider, "ja_jp");
        mergeLang(provider, "lzh");
        mergeLang(provider, "pl_pl");
        mergeLang(provider, "pt_pt", "pt_br");
        mergeLang(provider, "ru_ru");
        mergeLang(provider, "tr_tr");
        mergeLang(provider, "zh_cn");
        mergeLang(provider, "zh_hk");
        mergeLang(provider, "zh_tw");
        mergeLang(provider, "uk_ua");
    }

    private static void mergeLang(BiConsumer<Identifier, Map<String, String>> provider, String source, String... copies) {
        Map<String, String> translations = new HashMap<>();

        merge(translations, DATAPACK_LANG.resolve(source + ".json"));
        LanguageMergeGenerator.MOD_CONTAINER.findPath("assets/" + ToolTrims.MOD_ID + "/lang/patch/" + source + ".json").ifPresent(path -> merge(translations, path));

        provider.accept(ToolTrims.id(source), translations);
        for (var code : copies)
            provider.accept(ToolTrims.id(code), translations);
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
