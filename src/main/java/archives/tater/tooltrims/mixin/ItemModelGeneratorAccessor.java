package archives.tater.tooltrims.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import net.minecraft.data.models.ItemModelGenerators;

@Mixin(ItemModelGenerators.class)
public interface ItemModelGeneratorAccessor {
    @Accessor("GENERATED_TRIM_MODELS")
    static List<ItemModelGenerators.TrimModelData> TRIM_MATERIALS() {
        throw new AssertionError();
    }
}
