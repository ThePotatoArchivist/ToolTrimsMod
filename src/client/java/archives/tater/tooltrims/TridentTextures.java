package archives.tater.tooltrims;

import archives.tater.tooltrims.networking.TridentTrimPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

public class TridentTextures {
    private static final Map<RegistryKey<ArmorTrimPattern>, Map<RegistryKey<ArmorTrimMaterial>, Identifier>> TEXTURE_IDS = new HashMap<>();

    private static final WeakHashMap<TridentEntity, Identifier> TEXTURE_CACHE = new WeakHashMap<>();

    public static Identifier getCachedTextureId(TridentEntity tridentEntity, Identifier fallback) {
        return TEXTURE_CACHE.getOrDefault(tridentEntity, fallback);
    }

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

    public static @Nullable Identifier getTextureId(DynamicRegistryManager registryManager, ItemStack trident) {
        var armorTrim = ArmorTrim.getTrim(registryManager, trident).orElse(null);
        if (armorTrim == null)
            return null;
        return getTextureId(armorTrim.getPattern(), armorTrim.getMaterial());
    }

    private static Map<RegistryEntry<ArmorTrimPattern>, Map<RegistryEntry<ArmorTrimMaterial>, Identifier>> getTextureIds(RegistryWrapper.WrapperLookup wrapperLookup) {
        var materialRegistry = wrapperLookup.getWrapperOrThrow(RegistryKeys.TRIM_MATERIAL);
        return wrapperLookup.getWrapperOrThrow(RegistryKeys.TRIM_PATTERN).streamEntries().collect(Collectors.toUnmodifiableMap(
                pattern -> pattern,
                pattern -> materialRegistry.streamEntries().collect(Collectors.toUnmodifiableMap(
                        material -> material,
                        material -> ToolTrims.id("textures/entity/trident/trident_"
                                + pattern.getKey().map(it -> it.getValue().getPath()).orElse("missingno")
                                + "_"
                                + material.getKey().map(it -> it.getValue().getPath()).orElse("missingno")
                                + ".png"
                        )
                ))
        ));
    }

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(TridentTrimPacket.TYPE, (packet, player, responseSender) -> {
            var entity = player.getWorld().getEntityById(packet.tridentId());
            if (!(entity instanceof TridentEntity tridentEntity)) return;
            TEXTURE_CACHE.put(tridentEntity, getTextureId(packet.pattern(), packet.material()));
        });
    }
}
