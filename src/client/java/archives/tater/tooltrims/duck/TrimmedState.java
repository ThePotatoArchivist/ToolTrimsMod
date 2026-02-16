package archives.tater.tooltrims.duck;

import net.minecraft.world.item.equipment.trim.ArmorTrim;

import org.jspecify.annotations.Nullable;

public interface TrimmedState {
    @Nullable ArmorTrim tooltrims$getTrim();
    void tooltrims$setTrim(@Nullable ArmorTrim trim);
}
