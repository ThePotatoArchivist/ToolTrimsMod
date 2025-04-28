package archives.tater.tooltrims.loot;

import net.minecraft.loot.entry.LootPoolEntry;

import java.util.List;
import java.util.function.ObjLongConsumer;

public interface ReplaceablePools {
    void tooltrims$modifyPoolEntries(ObjLongConsumer<List<LootPoolEntry>> modifier);
}
