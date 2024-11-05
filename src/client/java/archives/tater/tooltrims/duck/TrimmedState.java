package archives.tater.tooltrims.duck;

import net.minecraft.item.equipment.trim.ArmorTrim;
import org.jetbrains.annotations.Nullable;

public interface TrimmedState {
    @Nullable ArmorTrim tooltrims$getTrim();
    void tooltrims$setTrim(@Nullable ArmorTrim trim);
}
