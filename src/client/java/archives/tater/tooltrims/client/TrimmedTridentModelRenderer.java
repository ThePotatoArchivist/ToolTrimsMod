package archives.tater.tooltrims.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.projectile.TridentModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.trim.ArmorTrim;

import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

import static java.util.Objects.requireNonNullElse;

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
    public void submit(@Nullable ArmorTrim argument, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        poseStack.pushPose();
        poseStack.scale(1.0F, -1.0F, -1.0F);
        submitNodeCollector.submitModelPart(this.model.root(), poseStack, this.model.renderType(requireNonNullElse(TridentTextures.getTextureId(argument), TridentModel.TEXTURE)), lightCoords, overlayCoords, null, false, hasFoil, -1, null, outlineColor);
        poseStack.popPose();
    }

    @Override
    public void getExtents(Consumer<Vector3fc> vertices) {
        PoseStack matrixStack = new PoseStack();
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        this.model.root().getExtentsForGui(matrixStack, vertices);
    }

    @Environment(EnvType.CLIENT)
    public record Unbaked() implements SpecialModelRenderer.Unbaked<ArmorTrim> {
        public static final MapCodec<TrimmedTridentModelRenderer.Unbaked> CODEC = MapCodec.unit(new TrimmedTridentModelRenderer.Unbaked());

        public MapCodec<TrimmedTridentModelRenderer.Unbaked> type() {
            return CODEC;
        }

        @Override
        public SpecialModelRenderer<ArmorTrim> bake(BakingContext context) {
            return new TrimmedTridentModelRenderer(new TridentModel(context.entityModelSet().bakeLayer(ModelLayers.TRIDENT)));
        }
    }
}
