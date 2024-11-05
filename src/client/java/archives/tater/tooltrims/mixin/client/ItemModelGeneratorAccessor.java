package archives.tater.tooltrims.mixin.client;

import net.minecraft.client.data.ItemModelGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ItemModelGenerator.class)
public interface ItemModelGeneratorAccessor {
    @Accessor("TRIM_MATERIALS")
    static List<ItemModelGenerator.TrimMaterial> TRIM_MATERIALS() {
        throw new AssertionError();
    }
}
