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

    public static Identifier TRIM_PATTERN_PREDICATE = new Identifier(ToolTrims.MOD_ID, "trim_pattern");

    private static <T> boolean equals(RegistryKey<T> first, RegistryKey<T> second) {
        return first.getRegistry().equals(second.getRegistry()) && first.getValue().equals(second.getValue());
    }

    public static float getModelIndex(RegistryKey<ArmorTrimPattern> pattern) {
        if (equals(pattern, ToolTrimsPatterns.LINEAR)) return 0.1f;
        if (equals(pattern, ToolTrimsPatterns.TRACKS)) return 0.2f;
        if (equals(pattern, ToolTrimsPatterns.CHARGE)) return 0.3f;
        if (equals(pattern, ToolTrimsPatterns.FROST)) return 0.4f;
        return 0.0f;
    }

    public static void register() {}
}
