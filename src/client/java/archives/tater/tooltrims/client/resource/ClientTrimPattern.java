package archives.tater.tooltrims.client.resource;

import archives.tater.tooltrims.ToolTrimsUtil;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ClientTrimPattern(String suffix) {
    public static final Codec<ClientTrimPattern> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ToolTrimsUtil.IDENTIFIER_PATH_CODEC.fieldOf("suffix").forGetter(ClientTrimPattern::suffix)
    ).apply(instance, ClientTrimPattern::new));

    public static final String PATH = "tooltrims/trim_pattern";
}
