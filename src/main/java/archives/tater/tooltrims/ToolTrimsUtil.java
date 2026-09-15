package archives.tater.tooltrims;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.Identifier;

import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@ApiStatus.NonExtendable
public interface ToolTrimsUtil {

    Codec<String> IDENTIFIER_PATH_CODEC = Codec.STRING.validate(suffix -> Identifier.isValidPath(suffix)
            ? DataResult.success(suffix)
            : DataResult.error(() -> "Non [a-z0-9/._-] character in path for location: " + suffix, suffix)
    );

    static Stream<Path> tryList(Path dir) {
        try {
            return Files.list(dir);
        } catch (IOException e) {
            return Stream.of();
        }
    }
}
