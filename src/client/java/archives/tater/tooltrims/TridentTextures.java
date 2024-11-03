package archives.tater.tooltrims;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public class TridentTextures {
    private static final Map<RegistryKey<ArmorTrimPattern>, Map<RegistryKey<ArmorTrimMaterial>, Identifier>> TEXTURE_IDS = new HashMap<>();

    public static Identifier getTextureId(RegistryKey<ArmorTrimPattern> pattern, RegistryKey<ArmorTrimMaterial> material) {
        return TEXTURE_IDS
                .computeIfAbsent(pattern, x -> new HashMap<>())
                .computeIfAbsent(material, x -> ToolTrims.id("textures/entity/trident/trident_"
                    + pattern.getValue().getPath()
                    + "_"
                    + material.getValue().getPath()
                    + ".png"));
    }

    public static Identifier getTextureId(RegistryEntry<ArmorTrimPattern> pattern, RegistryEntry<ArmorTrimMaterial> material) {
        return getTextureId(pattern.getKey().orElseThrow(), material.getKey().orElseThrow());
    }

    public static @Nullable Identifier getTextureId(ArmorTrim trim) {
        if (trim == null) return null;
        return getTextureId(trim.getPattern(), trim.getMaterial());
    }

    public static @Nullable Identifier getTextureId(ItemStack stack) {
        return getTextureId(stack.get(DataComponentTypes.TRIM));
    }

    public static @Nullable Identifier getTextureId(TridentEntity tridentEntity) {
        return getTextureId(tridentEntity.getAttached(ToolTrimsDataAttachment.TRIDENT_TRIM));
    }
}
