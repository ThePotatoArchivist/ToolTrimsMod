package archives.tater.tooltrims;

import archives.tater.tooltrims.datagen.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ToolTrimsData implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		var pack = fabricDataGenerator.createPack();
		pack.addProvider(TrimPatternGenerator::new);
		pack.addProvider(RecipeGenerator::new);
		pack.addProvider(ItemTagGenerator::new);
		pack.addProvider(AdvancementGenerator::new);
		pack.addProvider(LootTableGenerator::new);

		var overlayPack = fabricDataGenerator.createBuiltinResourcePack(ToolTrims.id("tooltrims"));
		overlayPack.addProvider(ModelGenerator::new);

		var enchancementPack = fabricDataGenerator.createBuiltinResourcePack(ToolTrims.id("enchancement"));
		enchancementPack.addProvider(EnchancementModelGenerator::new);
	}
}
