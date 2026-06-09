package archives.tater.tooltrims.client.data.models.item;

import archives.tater.tooltrims.client.ToolTrimsClient;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.projectile.TridentModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.EmptyModel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.properties.select.TrimMaterialProperty;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import org.joml.Matrix4fc;
import org.joml.Vector3fc;

import java.util.function.Consumer;

import static net.minecraft.client.data.models.model.ItemModelUtils.select;
import static net.minecraft.client.data.models.model.ItemModelUtils.when;

public class TexturedTridentModelRenderer implements NoDataSpecialModelRenderer {
    private final TridentModel model;
    private final SpriteGetter sprites;
    private final SpriteId sprite;

    public TexturedTridentModelRenderer(TridentModel model, SpriteGetter sprites, SpriteId sprite) {
        this.model = model;
        this.sprites = sprites;
        this.sprite = sprite;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        poseStack.pushPose();
        poseStack.scale(1.0F, -1.0F, -1.0F);
        submitNodeCollector.order(1).submitModelPart(model.root(), poseStack, ToolTrimsClient.TRIDENT_TRIMS_RENDER_TYPE, lightCoords, overlayCoords, sprites.get(sprite), true, hasFoil, -1, null, outlineColor);
        poseStack.popPose();
    }

    @Override
    public void getExtents(Consumer<Vector3fc> vertices) {
        PoseStack matrixStack = new PoseStack();
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        model.root().getExtentsForGui(matrixStack, vertices);
    }

    public record Unbaked(Identifier texture) implements SpecialModelRenderer.Unbaked<Void> {
        public static final MapCodec<TexturedTridentModelRenderer.Unbaked> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Identifier.CODEC.fieldOf("sprite").forGetter(Unbaked::texture)
        ).apply(instance, Unbaked::new));

        public MapCodec<TexturedTridentModelRenderer.Unbaked> type() {
            return CODEC;
        }

        @Override
        public SpecialModelRenderer<Void> bake(BakingContext context) {
            return new TexturedTridentModelRenderer(
                    new TridentModel(context.entityModelSet().bakeLayer(ModelLayers.TRIDENT)),
                    context.sprites(),
                    new SpriteId(ToolTrimsClient.TRIDENT_TRIMS_SHEET, texture)
            );
        }
    }

    public record UnbakedTrims(Identifier baseModel, Identifier basePath) implements ItemModel.Unbaked {
        public static final MapCodec<UnbakedTrims> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Identifier.CODEC.fieldOf("base_model").forGetter(UnbakedTrims::baseModel),
                Identifier.CODEC.fieldOf("base_path").forGetter(UnbakedTrims::basePath)
        ).apply(instance, UnbakedTrims::new));

        @Override
        public MapCodec<? extends ItemModel.Unbaked> type() {
            return CODEC;
        }

        @Override
        public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
            return select(
                    TrimPatternProperty.INSTANCE,
                    new EmptyModel.Unbaked(),
                    ToolTrimsClient.TRIM_PATTERNS.joinEntries().entrySet().stream().map(pattern ->
                            when(
                                    ResourceKey.create(Registries.TRIM_PATTERN, pattern.getKey()), select(
                                    new TrimMaterialProperty(),
                                    new EmptyModel.Unbaked(),
                                    ToolTrimsClient.TRIM_MATERIALS.joinEntries().entrySet().stream().map(material ->
                                            when(ResourceKey.create(Registries.TRIM_MATERIAL, material.getKey()), ItemModelUtils.specialModel(
                                                    baseModel,
                                                    new Unbaked(UnbakedTrimsModel.createModelId(basePath, pattern.getValue(), material.getValue()))
                                            ))
                                    ).toList()
                            ))
                    ).toList()
            ).bake(context, transformation);
        }

        @Override
        public void resolveDependencies(Resolver resolver) {}
    }
}
