package archives.tater.tooltrims;

import archives.tater.tooltrims.datagen.ItemTagGenerator;
import archives.tater.tooltrims.datagen.ModelGenerator;
import archives.tater.tooltrims.datagen.RecipeGenerator;
import archives.tater.tooltrims.datagen.TrimPatternGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ToolTrimsData implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		var pack = fabricDataGenerator.createPack();
		pack.addProvider((FabricDataGenerator.Pack.Factory<TrimPatternGenerator>) TrimPatternGenerator::new);
		pack.addProvider(RecipeGenerator::new);
		pack.addProvider(ItemTagGenerator::new);
		pack.addProvider(ModelGenerator::new);
	}
}
