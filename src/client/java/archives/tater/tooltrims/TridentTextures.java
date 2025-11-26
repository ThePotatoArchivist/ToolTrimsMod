package archives.tater.tooltrims;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;

@SuppressWarnings("UnstableApiUsage")
public class TridentTextures {
    private static final Map<ResourceKey<TrimPattern>, Map<ResourceKey<TrimMaterial>, ResourceLocation>> TEXTURE_IDS = new HashMap<>();

    public static ResourceLocation getTextureId(ResourceKey<TrimPattern> pattern, ResourceKey<TrimMaterial> material) {
        return TEXTURE_IDS
                .computeIfAbsent(pattern, x -> new HashMap<>())
                .computeIfAbsent(material, x -> ToolTrims.id("textures/entity/trident/trident_"
                    + pattern.location().getPath()
                    + "_"
                    + material.location().getPath()
                    + ".png"));
    }

    public static ResourceLocation getTextureId(Holder<TrimPattern> pattern, Holder<TrimMaterial> material) {
        return getTextureId(pattern.unwrapKey().orElseThrow(), material.unwrapKey().orElseThrow());
    }

    public static @Nullable ResourceLocation getTextureId(ArmorTrim trim) {
        if (trim == null) return null;
        return getTextureId(trim.pattern(), trim.material());
    }

    public static @Nullable ResourceLocation getTextureId(ItemStack stack) {
        return getTextureId(stack.get(DataComponents.TRIM));
    }

    public static @Nullable ResourceLocation getTextureId(ThrownTrident tridentEntity) {
        return getTextureId(tridentEntity.getAttached(ToolTrimsDataAttachment.TRIDENT_TRIM));
    }
}
