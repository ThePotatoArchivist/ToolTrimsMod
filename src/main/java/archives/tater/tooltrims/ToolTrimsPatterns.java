package archives.tater.tooltrims;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.trim.TrimPattern;

import java.util.List;

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

    public static void register() {}
}
