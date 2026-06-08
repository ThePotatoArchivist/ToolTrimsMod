package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.client.data.ClientTrimOverlay;
import archives.tater.tooltrims.client.data.models.item.UnbakedTrimsModel;
import archives.tater.tooltrims.mixin.client.ModelTemplateAccessor;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;

import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ClientTrimOverlayGenerator extends FabricCodecDataProvider<ClientTrimOverlay> {
    protected ClientTrimOverlayGenerator(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture, PackOutput.Target.RESOURCE_PACK, ClientTrimOverlay.Loader.PATH, ClientTrimOverlay.CODEC);
    }

    private static final Identifier HANDHELD = ((ModelTemplateAccessor) ModelTemplates.FLAT_HANDHELD_ITEM).getModel().orElseThrow();
    private static final Identifier HANDHELD_MACE = ((ModelTemplateAccessor) ModelTemplates.FLAT_HANDHELD_MACE_ITEM).getModel().orElseThrow();

    private static final Item[] HANDHELD_ITEMS = {
            Items.WOODEN_SWORD,
            Items.WOODEN_SHOVEL,
            Items.WOODEN_PICKAXE,
            Items.WOODEN_AXE,
            Items.WOODEN_HOE,
            Items.STONE_SWORD,
            Items.STONE_SHOVEL,
            Items.STONE_PICKAXE,
            Items.STONE_AXE,
            Items.STONE_HOE,
            Items.COPPER_SWORD,
            Items.COPPER_SHOVEL,
            Items.COPPER_PICKAXE,
            Items.COPPER_AXE,
            Items.COPPER_HOE,
            Items.GOLDEN_SWORD,
            Items.GOLDEN_SHOVEL,
            Items.GOLDEN_PICKAXE,
            Items.GOLDEN_AXE,
            Items.GOLDEN_HOE,
            Items.IRON_SWORD,
            Items.IRON_SHOVEL,
            Items.IRON_PICKAXE,
            Items.IRON_AXE,
            Items.IRON_HOE,
            Items.DIAMOND_SWORD,
            Items.DIAMOND_SHOVEL,
            Items.DIAMOND_PICKAXE,
            Items.DIAMOND_AXE,
            Items.DIAMOND_HOE,
            Items.NETHERITE_SWORD,
            Items.NETHERITE_SHOVEL,
            Items.NETHERITE_PICKAXE,
            Items.NETHERITE_AXE,
            Items.NETHERITE_HOE
    };

    @SuppressWarnings("deprecation")
    private static Identifier itemId(Item item) {
        return item.builtInRegistryHolder().key().identifier();
    }

    private static void registerPlain(BiConsumer<Identifier, ClientTrimOverlay> provider, Item item, Identifier parent) {
        var itemId = itemId(item);
        var name = itemId.getPath();
        provider.accept(ToolTrims.id(name), new ClientTrimOverlay(
                new UnbakedTrimsModel(ToolTrims.id("trims/item/" + name), parent),
                List.of(itemId)
        ));
    }

    private static void registerEmpty(BiConsumer<Identifier, ClientTrimOverlay> provider, String name) {
        provider.accept(ToolTrims.id(name), new ClientTrimOverlay(
                new UnbakedTrimsModel(ToolTrims.id("trims/item/diamond_" + name), HANDHELD),
                List.of()
        ));
    }

    @Override
    protected void configure(BiConsumer<Identifier, ClientTrimOverlay> provider, HolderLookup.Provider registryLookup) {
        for (var item : HANDHELD_ITEMS)
            registerPlain(provider, item, HANDHELD);
        registerPlain(provider, Items.MACE, HANDHELD_MACE);

        registerEmpty(provider, "sword");
        registerEmpty(provider, "pickaxe");
        registerEmpty(provider, "axe");
        registerEmpty(provider, "shovel");
        registerEmpty(provider, "hoe");
    }

    @Override
    public String getName() {
        return "Trim Overlays";
    }
}
