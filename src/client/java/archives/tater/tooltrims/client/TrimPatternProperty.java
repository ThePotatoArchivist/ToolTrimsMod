package archives.tater.tooltrims.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimPattern;

import org.jspecify.annotations.Nullable;

public class TrimPatternProperty implements SelectItemModelProperty<ResourceKey<TrimPattern>> {
    private TrimPatternProperty() {}

    // Singleton
    public static final TrimPatternProperty INSTANCE = new TrimPatternProperty();

    public static final Codec<ResourceKey<TrimPattern>> VALUE_CODEC = ResourceKey.codec(Registries.TRIM_PATTERN);

    public static final SelectItemModelProperty.Type<TrimPatternProperty, ResourceKey<TrimPattern>> TYPE = SelectItemModelProperty.Type.create(
            MapCodec.unit(INSTANCE), ResourceKey.codec(Registries.TRIM_PATTERN)
    );

    @Override
    public @Nullable ResourceKey<TrimPattern> get(ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity user, int seed, ItemDisplayContext displayContext) {
        ArmorTrim armorTrim = stack.get(DataComponents.TRIM);
        return armorTrim == null ? null : armorTrim.pattern().unwrapKey().orElse(null);
    }

    @Override
    public Codec<ResourceKey<TrimPattern>> valueCodec() {
        return VALUE_CODEC;
    }

    @Override
    public Type<? extends SelectItemModelProperty<ResourceKey<TrimPattern>>, ResourceKey<TrimPattern>> type() {
        return TYPE;
    }
}
