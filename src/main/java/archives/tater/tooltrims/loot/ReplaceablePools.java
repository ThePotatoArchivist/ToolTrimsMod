package archives.tater.tooltrims.loot;

import java.util.List;
import java.util.function.ObjLongConsumer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

public interface ReplaceablePools {
    void tooltrims$modifyPoolEntries(ObjLongConsumer<List<LootPoolEntryContainer>> modifier);
}
