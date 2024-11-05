package archives.tater.tooltrims;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.render.item.property.select.SelectProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.trim.ArmorTrim;
import net.minecraft.item.equipment.trim.ArmorTrimPattern;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import org.jetbrains.annotations.Nullable;

public class TrimPatternProperty implements SelectProperty<RegistryKey<ArmorTrimPattern>> {
    private TrimPatternProperty() {}

    // Singleton
    public static final TrimPatternProperty INSTANCE = new TrimPatternProperty();

    public static final Codec<RegistryKey<ArmorTrimPattern>> VALUE_CODEC = RegistryKey.createCodec(RegistryKeys.TRIM_PATTERN);

    public static final SelectProperty.Type<TrimPatternProperty, RegistryKey<ArmorTrimPattern>> TYPE = SelectProperty.Type.create(
            MapCodec.unit(INSTANCE), RegistryKey.createCodec(RegistryKeys.TRIM_PATTERN)
    );

    @Override
    public @Nullable RegistryKey<ArmorTrimPattern> getValue(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity user, int seed, ItemDisplayContext displayContext) {
        ArmorTrim armorTrim = stack.get(DataComponentTypes.TRIM);
        return armorTrim == null ? null : armorTrim.pattern().getKey().orElse(null);
    }

    @Override
    public Codec<RegistryKey<ArmorTrimPattern>> valueCodec() {
        return VALUE_CODEC;
    }

    @Override
    public Type<? extends SelectProperty<RegistryKey<ArmorTrimPattern>>, RegistryKey<ArmorTrimPattern>> getType() {
        return TYPE;
    }
}
