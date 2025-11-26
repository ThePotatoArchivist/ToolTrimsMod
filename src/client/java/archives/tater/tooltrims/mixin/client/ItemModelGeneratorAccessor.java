package archives.tater.tooltrims.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import net.minecraft.client.data.models.ItemModelGenerators;

@Mixin(ItemModelGenerators.class)
public interface ItemModelGeneratorAccessor {
    @Accessor("TRIM_MATERIAL_MODELS")
    static List<ItemModelGenerators.TrimMaterialData> TRIM_MATERIALS() {
        throw new AssertionError();
    }
}
