package archives.tater.tooltrims.mixin.client;

import archives.tater.tooltrims.client.ToolTrimsClient;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.resources.model.sprite.AtlasManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Mixin(AtlasManager.class)
public class AtlasManagerMixin {
    @Shadow
    @Final
    @Mutable
    private static List<AtlasManager.AtlasConfig> KNOWN_ATLASES;

    @Inject(
            method = "<clinit>",
            at = @At("TAIL")
    )
    private static void addAtlas(CallbackInfo ci) {
        if (!(KNOWN_ATLASES instanceof ArrayList) && !(KNOWN_ATLASES instanceof LinkedList))
            KNOWN_ATLASES = new ArrayList<>(KNOWN_ATLASES);
        KNOWN_ATLASES.add(new AtlasManager.AtlasConfig(ToolTrimsClient.TRIDENT_TRIMS_SHEET, ToolTrimsClient.TRIDENT_TRIMS_ATLAS, false));
    }
}
