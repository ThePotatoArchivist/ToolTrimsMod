package archives.tater.tooltrims;

import net.fabricmc.loader.api.FabricLoader;

// This class is used in early loading mixin config, so it should not import any game classes
public class ToolTrimsModCompat {
    public static final boolean isEnchancementLoaded = FabricLoader.getInstance().isModLoaded("enchancement");
}
