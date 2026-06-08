package archives.tater.tooltrims.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.SpriteSources;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

@Mixin(SpriteSources.class)
public interface SpriteSourcesAccessor {
    @Accessor
    static ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends SpriteSource>> getID_MAPPER() {
        throw new UnsupportedOperationException();
    }
}
