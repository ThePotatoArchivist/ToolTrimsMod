package archives.tater.tooltrims.client;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.ToolTrimsDataAttachment;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public class TridentTextures {
    private static final Map<ResourceKey<TrimPattern>, Map<ResourceKey<TrimMaterial>, Identifier>> TEXTURE_IDS = new HashMap<>();

    public static Identifier getTextureId(ResourceKey<TrimPattern> pattern, ResourceKey<TrimMaterial> material) {
        return TEXTURE_IDS
                .computeIfAbsent(pattern, x -> new HashMap<>())
                .computeIfAbsent(material, x -> ToolTrims.id("textures/entity/trident/trident_"
                    + pattern.identifier().getPath()
                    + "_"
                    + material.identifier().getPath()
                    + ".png"));
    }

    public static Identifier getTextureId(Holder<TrimPattern> pattern, Holder<TrimMaterial> material) {
        return getTextureId(pattern.unwrapKey().orElseThrow(), material.unwrapKey().orElseThrow());
    }

    public static @Nullable Identifier getTextureId(ArmorTrim trim) {
        if (trim == null) return null;
        return getTextureId(trim.pattern(), trim.material());
    }

    public static @Nullable Identifier getTextureId(ItemStack stack) {
        return getTextureId(stack.get(DataComponents.TRIM));
    }

    public static @Nullable Identifier getTextureId(ThrownTrident tridentEntity) {
        return getTextureId(tridentEntity.getAttached(ToolTrimsDataAttachment.TRIDENT_TRIM));
    }
}
