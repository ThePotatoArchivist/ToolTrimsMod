package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrims;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class ToolTrimsData implements DataGeneratorEntrypoint {
	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		DataGeneratorEntrypoint.super.buildRegistry(registryBuilder);
		registryBuilder.add(Registries.TRIM_PATTERN, TrimPatternGenerator::boostrap);
	}

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		var pack = fabricDataGenerator.createPack();
		pack.addProvider(TrimPatternGenerator::new);
		pack.addProvider(TTRecipeGenerator.Provider::new);
		pack.addProvider(ItemTagGenerator::new);
		pack.addProvider(AdvancementGenerator::new);
		pack.addProvider(LootTableGenerator::new);
		pack.addProvider(LanguageCopyGenerator.of("es_ar", "es_es"));
		pack.addProvider(LanguageCopyGenerator.of("es_cl", "es_es"));
		pack.addProvider(LanguageCopyGenerator.of("es_ec", "es_es"));
		pack.addProvider(LanguageCopyGenerator.of("es_mx", "es_es"));
		pack.addProvider(LanguageCopyGenerator.of("es_uy", "es_es"));
		pack.addProvider(LanguageCopyGenerator.of("es_ve", "es_es"));
		pack.addProvider(LanguageCopyGenerator.of("fr_ca", "fr_fr"));
		pack.addProvider(LanguageCopyGenerator.of("pt_br", "pt_pt"));

		var overlayPack = fabricDataGenerator.createBuiltinResourcePack(ToolTrims.id("tooltrims"));
		overlayPack.addProvider(ModelGenerator::new);

		var enchancementPack = fabricDataGenerator.createBuiltinResourcePack(ToolTrims.id("enchancement"));
		enchancementPack.addProvider(EnchancementModelGenerator::new);
	}
}
