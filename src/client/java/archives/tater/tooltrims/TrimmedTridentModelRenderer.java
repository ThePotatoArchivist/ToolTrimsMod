package archives.tater.tooltrims;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.entity.model.TridentEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
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
    public void render(@Nullable ArmorTrim data, ItemDisplayContext displayContext, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, boolean glint) {
        matrices.push();
        matrices.scale(1.0F, -1.0F, -1.0F);
        this.model.render(
                matrices,
                ItemRenderer.getItemGlintConsumer(
                        vertexConsumers,
                        this.model.getLayer(Objects.requireNonNullElse(TridentTextures.getTextureId(data), TridentEntityModel.TEXTURE)),
                        false,
                        glint
                ),
                light,
                overlay
        );
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

        public SpecialModelRenderer<?> bake(LoadedEntityModels entityModels) {
            return new TrimmedTridentModelRenderer(new TridentEntityModel(entityModels.getModelPart(EntityModelLayers.TRIDENT)));
        }
    }
}
