package archives.tater.tooltrims;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.trim.ArmorTrim;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Objects;
import java.util.Set;

public class TrimmedTridentModelRenderer implements SpecialModelRenderer<ArmorTrim> {
    private final TridentModel model;

    public TrimmedTridentModelRenderer(TridentModel model) {
        this.model = model;
    }

    @Override
    public @Nullable ArmorTrim extractArgument(ItemStack stack) {
        return stack.get(DataComponents.TRIM);
    }

    @Override
    public void submit(@Nullable ArmorTrim data, ItemDisplayContext displayContext, PoseStack matrices, SubmitNodeCollector queue, int light, int overlay, boolean glint, int i) {
        matrices.pushPose();
        matrices.scale(1.0F, -1.0F, -1.0F);
        queue.submitModelPart(this.model.root(), matrices, this.model.renderType(Objects.requireNonNullElse(TridentTextures.getTextureId(data), TridentModel.TEXTURE)), light, overlay, null, false, glint, -1, null, i);
        matrices.popPose();
    }

    @Override
    public void getExtents(Set<Vector3f> vertices) {
        PoseStack matrixStack = new PoseStack();
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        this.model.root().getExtentsForGui(matrixStack, vertices);
    }

    @Environment(EnvType.CLIENT)
    public record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<TrimmedTridentModelRenderer.Unbaked> CODEC = MapCodec.unit(new TrimmedTridentModelRenderer.Unbaked());

        public MapCodec<TrimmedTridentModelRenderer.Unbaked> type() {
            return CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(BakingContext context) {
            return new TrimmedTridentModelRenderer(new TridentModel(context.entityModelSet().bakeLayer(ModelLayers.TRIDENT)));
        }
    }
}
