package archives.tater.tooltrims.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class ToolTrimsData implements DataGeneratorEntrypoint {
	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.TRIM_PATTERN, TrimPatternGenerator::boostrap);
	}

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		var pack = fabricDataGenerator.createPack();
		pack.addProvider(AtlasGenerator::new);
		pack.addProvider(TrimPatternGenerator::new);
		pack.addProvider(TTRecipeGenerator.Provider::new);
		pack.addProvider(ItemTagGenerator::new);
		pack.addProvider(AdvancementGenerator::new);
		pack.addProvider(ModelGenerator::new);
		pack.addProvider(ClientTrimPatternGenerator::new);
		pack.addProvider(ClientTrimMaterialGenerator::new);
		pack.addProvider(ClientTrimOverlayGenerator::new);
		pack.addProvider(LanguageMergeGenerator::new);
	}
}
