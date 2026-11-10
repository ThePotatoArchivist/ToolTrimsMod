package archives.tater.tooltrims.client.resource;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModels;
import net.minecraft.resources.Identifier;

import java.util.List;

public record ClientTrimOverlay(ItemModel.Unbaked model, List<Identifier> items) {
    public static final Codec<ClientTrimOverlay> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemModels.CODEC.fieldOf("model").forGetter(ClientTrimOverlay::model),
            Identifier.CODEC.listOf().fieldOf("items").forGetter(ClientTrimOverlay::items)
    ).apply(instance, ClientTrimOverlay::new));

    public static final String PATH = "tooltrims/trim_overlay";
}
