package archives.tater.tooltrims.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.resources.RegistryOps;

@Mixin(RegistryOps.class)
public interface RegistryOpsAccessor {
    @Accessor
    RegistryOps.RegistryInfoLookup getLookupProvider();
}
