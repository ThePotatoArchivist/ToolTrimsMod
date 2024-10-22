package archives.tater.tooltrims;

import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.List;

public class ToolTrimsPatterns {
    private static RegistryKey<ArmorTrimPattern> of(Identifier id) {
        return RegistryKey.of(RegistryKeys.TRIM_PATTERN, id);
    }

    private static RegistryKey<ArmorTrimPattern> of(String path) {
        return of(new Identifier(ToolTrims.MOD_ID, path));
    }

    public static RegistryKey<ArmorTrimPattern> LINEAR = of("linear");
    public static RegistryKey<ArmorTrimPattern> TRACKS = of("tracks");
    public static RegistryKey<ArmorTrimPattern> CHARGE = of("charge");
    public static RegistryKey<ArmorTrimPattern> FROST = of("frost");

    public static List<RegistryKey<ArmorTrimPattern>> PATTERNS = List.of(LINEAR, TRACKS, CHARGE, FROST);

    public static void register() {}
}
