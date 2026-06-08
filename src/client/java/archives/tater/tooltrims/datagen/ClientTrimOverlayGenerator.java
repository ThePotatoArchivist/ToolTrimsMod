package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.client.data.ClientTrimOverlay;
import archives.tater.tooltrims.client.data.models.item.UnbakedTrimsModel;
import archives.tater.tooltrims.mixin.client.ModelTemplateAccessor;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.renderer.item.ItemModel;
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

    private static Identifier getParent(ModelTemplate modelTemplate) {
        return ((ModelTemplateAccessor) modelTemplate).getModel().orElseThrow();
    }

    private static final Identifier FLAT = getParent(ModelTemplates.FLAT_ITEM);
    private static final Identifier HANDHELD = getParent(ModelTemplates.FLAT_HANDHELD_ITEM);
    private static final Identifier HANDHELD_MACE = getParent(ModelTemplates.FLAT_HANDHELD_MACE_ITEM);
    private static final Identifier SPEAR_IN_HAND = getParent(ModelTemplates.SPEAR_IN_HAND);

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

    private static final Item[] SPEARS = {
            Items.WOODEN_SPEAR,
            Items.STONE_SPEAR,
            Items.COPPER_SPEAR,
            Items.IRON_SPEAR,
            Items.GOLDEN_SPEAR,
            Items.DIAMOND_SPEAR,
            Items.NETHERITE_SPEAR
    };

    @SuppressWarnings("deprecation")
    private static Identifier itemId(Item item) {
        return item.builtInRegistryHolder().key().identifier();
    }

    private static Identifier trimmedId(String name) {
        return ToolTrims.id("trims/item/" + name);
    }

    private static void register(BiConsumer<Identifier, ClientTrimOverlay> provider, Identifier id, Item item, ItemModel.Unbaked model) {
        var itemId = itemId(item);
        provider.accept(id, new ClientTrimOverlay(
                model,
                List.of(itemId)
        ));
    }

    private static void register(BiConsumer<Identifier, ClientTrimOverlay> provider, Item item, ItemModel.Unbaked model) {
        register(provider, ToolTrims.id(itemId(item).getPath()), item, model);
    }

    private static void registerPlain(BiConsumer<Identifier, ClientTrimOverlay> provider, Item item, Identifier parent) {
        var itemId = itemId(item);
        var name = itemId.getPath();
        register(provider, ToolTrims.id(itemId.getPath()), item, new UnbakedTrimsModel(trimmedId(name), parent));
    }

    private static ItemModel.Unbaked spear(Item item) {
        var basePath = trimmedId(itemId(item).getPath());
        return ItemModelGenerators.createFlatModelDispatch(
                new UnbakedTrimsModel(basePath, FLAT),
                new UnbakedTrimsModel(basePath.withSuffix("_in_hand"), SPEAR_IN_HAND)
        );
    }

    private static void registerEmpty(BiConsumer<Identifier, ClientTrimOverlay> provider, Identifier name, ItemModel.Unbaked model) {
        provider.accept(name, new ClientTrimOverlay(model, List.of()));
    }

    private static void registerEmpty(BiConsumer<Identifier, ClientTrimOverlay> provider, Identifier name, Item reference) {
        registerEmpty(provider, name, new UnbakedTrimsModel(trimmedId(itemId(reference).getPath()), HANDHELD));
    }

    @Override
    protected void configure(BiConsumer<Identifier, ClientTrimOverlay> provider, HolderLookup.Provider registryLookup) {
        for (var item : HANDHELD_ITEMS)
            registerPlain(provider, item, HANDHELD);
        for (var item : SPEARS)
            register(provider, item, spear(item));

        registerPlain(provider, Items.MACE, HANDHELD_MACE);

        registerEmpty(provider, ClientTrimOverlay.Loader.FALLBACK_SWORD, Items.DIAMOND_SWORD);
        registerEmpty(provider, ClientTrimOverlay.Loader.FALLBACK_PICKAXE, Items.DIAMOND_PICKAXE);
        registerEmpty(provider, ClientTrimOverlay.Loader.FALLBACK_AXE, Items.DIAMOND_AXE);
        registerEmpty(provider, ClientTrimOverlay.Loader.FALLBACK_SHOVEL, Items.DIAMOND_SHOVEL);
        registerEmpty(provider, ClientTrimOverlay.Loader.FALLBACK_HOE, Items.DIAMOND_HOE);
        registerEmpty(provider, ClientTrimOverlay.Loader.FALLBACK_SPEAR, spear(Items.DIAMOND_SPEAR));
    }

    @Override
    public String getName() {
        return "Trim Overlays";
    }
}
