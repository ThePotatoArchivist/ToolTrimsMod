package archives.tater.tooltrims.client.resource;

import archives.tater.tooltrims.ToolTrimsUtil;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public record ClientTrimMaterial(Identifier assetName, String suffix) {
    public static final Codec<ClientTrimMaterial> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("asset_name").forGetter(ClientTrimMaterial::assetName),
            ToolTrimsUtil.IDENTIFIER_PATH_CODEC.fieldOf("suffix").forGetter(ClientTrimMaterial::suffix)
    ).apply(instance, ClientTrimMaterial::new));
    public static final String PATH = "tooltrims/trim_material";
}
