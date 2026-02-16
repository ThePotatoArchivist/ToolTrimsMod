package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrims;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.core.HolderLookup;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class LanguageCopyGenerator extends FabricLanguageProvider {
    private final String sourceLanguageCode;

    public LanguageCopyGenerator(String languageCode, String sourceLanguageCode, FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, languageCode, registryLookup);
        this.sourceLanguageCode = sourceLanguageCode;
    }

    @Override
    public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder translationBuilder) {
        try {
            translationBuilder.add(FabricLoader.getInstance().getModContainer(ToolTrims.MOD_ID).orElseThrow()
                    .findPath("assets/" + ToolTrims.MOD_ID + "/lang/" + sourceLanguageCode + ".json").orElseThrow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static FabricDataGenerator.Pack.RegistryDependentFactory<LanguageCopyGenerator> of(String languageCode, String sourceLanguageCode) {
        return (output, registriesFuture) -> new LanguageCopyGenerator(languageCode, sourceLanguageCode, output, registriesFuture);
    }
}
