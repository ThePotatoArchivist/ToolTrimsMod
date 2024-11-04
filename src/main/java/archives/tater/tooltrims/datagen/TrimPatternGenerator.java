package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.item.ToolTrimsItems;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static archives.tater.tooltrims.datagen.lib.FutureCollector.futureCollector;

public class TrimPatternGenerator implements DataProvider {
    private final DataOutput.PathResolver resolver;
    private final Map<Identifier, JsonElement> jsons = new HashMap<>();

    public TrimPatternGenerator(FabricDataOutput output) {
        this.resolver = output.getResolver(DataOutput.OutputType.DATA_PACK, "trim_pattern");
    }

    protected void register(Identifier id, Item templateItem) {
        var json = new JsonObject();
        json.addProperty("asset_id", "");
        var description = new JsonObject();
        description.addProperty("translate", Util.createTranslationKey("tool_trim_pattern", id));
        json.add("description", description);
        json.addProperty("template_item", Registries.ITEM.getId(templateItem).toString());
        jsons.put(id, json);
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        ToolTrimsItems.SMITHING_TEMPLATES.forEach((entry, templateItem) ->
                register(entry.getValue(), templateItem));
        return jsons.entrySet().stream().map(entry ->
                DataProvider.writeToPath(writer, entry.getValue(), resolver.resolveJson(entry.getKey()))
        ).collect(futureCollector());
    }

    @Override
    public String getName() {
        return "Trim Pattern Definitions";
    }
}
