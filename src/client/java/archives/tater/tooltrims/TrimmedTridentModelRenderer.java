package archives.tater.tooltrims;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.TridentEntityModel;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.trim.ArmorTrim;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Objects;
import java.util.Set;

public class TrimmedTridentModelRenderer implements SpecialModelRenderer<ArmorTrim> {
    private final TridentEntityModel model;

    public TrimmedTridentModelRenderer(TridentEntityModel model) {
        this.model = model;
    }

    @Override
    public @Nullable ArmorTrim getData(ItemStack stack) {
        return stack.get(DataComponentTypes.TRIM);
    }

    @Override
    public void render(@Nullable ArmorTrim data, ItemDisplayContext displayContext, MatrixStack matrices, OrderedRenderCommandQueue queue, int light, int overlay, boolean glint, int i) {
        matrices.push();
        matrices.scale(1.0F, -1.0F, -1.0F);
        queue.submitModelPart(this.model.getRootPart(), matrices, this.model.getLayer(Objects.requireNonNullElse(TridentTextures.getTextureId(data), TridentEntityModel.TEXTURE)), light, overlay, null, false, glint, -1, null, i);
        matrices.pop();
    }

    @Override
    public void collectVertices(Set<Vector3f> vertices) {
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        this.model.getRootPart().collectVertices(matrixStack, vertices);
    }

    @Environment(EnvType.CLIENT)
    public record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<TrimmedTridentModelRenderer.Unbaked> CODEC = MapCodec.unit(new TrimmedTridentModelRenderer.Unbaked());

        public MapCodec<TrimmedTridentModelRenderer.Unbaked> getCodec() {
            return CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(BakeContext context) {
            return new TrimmedTridentModelRenderer(new TridentEntityModel(context.entityModelSet().getModelPart(EntityModelLayers.TRIDENT)));
        }
    }
}
