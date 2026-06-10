package archives.tater.tooltrims.registry;

import archives.tater.tooltrims.ToolTrims;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.trim.TrimPattern;

import java.util.List;

public class ToolTrimsPatterns {
    private static ResourceKey<TrimPattern> create(Identifier id) {
        return ResourceKey.create(Registries.TRIM_PATTERN, id);
    }

    private static ResourceKey<TrimPattern> create(String path) {
        return create(ToolTrims.id(path));
    }

    public static final ResourceKey<TrimPattern> LINEAR = create("linear");
    public static final ResourceKey<TrimPattern> TRACKS = create("tracks");
    public static final ResourceKey<TrimPattern> CHARGE = create("charge");
    public static final ResourceKey<TrimPattern> FROST = create("frost");

    public static final List<ResourceKey<TrimPattern>> PATTERNS = List.of(LINEAR, TRACKS, CHARGE, FROST);

    public static void init() {}
}
