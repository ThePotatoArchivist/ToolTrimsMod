package archives.tater.tooltrims.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.resources.Identifier;

import java.util.Optional;

@Mixin(ModelTemplate.class)
public interface ModelTemplateAccessor {
    @Accessor
    Optional<Identifier> getModel();
}
