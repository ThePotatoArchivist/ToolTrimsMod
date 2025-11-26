package archives.tater.tooltrims;

import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.equipment.trim.TrimPattern;

public class ToolTrimsPatterns {
    private static ResourceKey<TrimPattern> of(Identifier id) {
        return ResourceKey.create(Registries.TRIM_PATTERN, id);
    }

    private static ResourceKey<TrimPattern> of(String path) {
        return of(ToolTrims.id(path));
    }

    public static final ResourceKey<TrimPattern> LINEAR = of("linear");
    public static final ResourceKey<TrimPattern> TRACKS = of("tracks");
    public static final ResourceKey<TrimPattern> CHARGE = of("charge");
    public static final ResourceKey<TrimPattern> FROST = of("frost");

    public static final List<ResourceKey<TrimPattern>> PATTERNS = List.of(LINEAR, TRACKS, CHARGE, FROST);

    private static <T> boolean equals(ResourceKey<T> first, ResourceKey<T> second) {
        return first.registry().equals(second.registry()) && first.identifier().equals(second.identifier());
    }

    public static float getModelIndex(ResourceKey<TrimPattern> pattern) {
        if (equals(pattern, LINEAR)) return 0.1f;
        if (equals(pattern, TRACKS)) return 0.2f;
        if (equals(pattern, CHARGE)) return 0.3f;
        if (equals(pattern, FROST)) return 0.4f;
        return 0.0f;
    }

    public static void register() {}
}
