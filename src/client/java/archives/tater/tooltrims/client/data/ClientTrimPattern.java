package archives.tater.tooltrims.client.data;

import archives.tater.tooltrims.ToolTrimsUtil;
import archives.tater.tooltrims.client.data.util.StoredJsonResourceReloadListener;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.FileToIdConverter;

public record ClientTrimPattern(String suffix) {
    public static final Codec<ClientTrimPattern> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ToolTrimsUtil.IDENTIFIER_PATH_CODEC.fieldOf("suffix").forGetter(ClientTrimPattern::suffix)
    ).apply(instance, ClientTrimPattern::new));

    public static class Loader extends StoredJsonResourceReloadListener<ClientTrimPattern> {
        public static final String PATH = "tooltrims/trim_pattern";
        private static final FileToIdConverter LISTER = FileToIdConverter.json(PATH);

        public Loader() {
            super(CODEC, LISTER);
        }
    }
}
