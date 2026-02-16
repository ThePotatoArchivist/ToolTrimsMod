package archives.tater.tooltrims.mixin.client;

import archives.tater.tooltrims.duck.TrimmedState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
import net.minecraft.world.item.equipment.trim.ArmorTrim;

import org.jspecify.annotations.Nullable;

@Mixin(ThrownTridentRenderState.class)
public class TridentEntityRenderStateMixin implements TrimmedState {
    @Unique
    private @Nullable ArmorTrim tooltrims$trim = null;

    @Override
    public @Nullable ArmorTrim tooltrims$getTrim() {
        return tooltrims$trim;
    }

    @Override
    public void tooltrims$setTrim(@Nullable ArmorTrim trim) {
        tooltrims$trim = trim;
    }
}
