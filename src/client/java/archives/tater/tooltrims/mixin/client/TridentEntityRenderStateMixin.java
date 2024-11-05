package archives.tater.tooltrims.mixin.client;

import archives.tater.tooltrims.duck.TrimmedState;
import net.minecraft.client.render.entity.state.TridentEntityRenderState;
import net.minecraft.item.equipment.trim.ArmorTrim;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(TridentEntityRenderState.class)
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
