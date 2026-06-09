package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.client.data.resource.ClientTrimOverlay;
import archives.tater.tooltrims.client.data.models.item.TexturedTridentModelRenderer;
import archives.tater.tooltrims.client.data.models.item.UnbakedTrimsModel;
import archives.tater.tooltrims.mixin.client.ModelTemplateAccessor;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.properties.numeric.CrossbowPull;
import net.minecraft.client.renderer.item.properties.numeric.UseDuration;
import net.minecraft.client.renderer.item.properties.select.Charge;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static net.minecraft.client.data.models.model.ItemModelUtils.*;
import static net.minecraft.client.data.models.model.ModelLocationUtils.getModelLocation;

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

    public static Identifier trimmedId(String name) {
        return ToolTrims.id("trims/items/" + name);
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
        register(provider, ToolTrims.id(name), item, new UnbakedTrimsModel(trimmedId(name), parent));
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
        register(provider, Items.TRIDENT, ItemModelGenerators.createFlatModelDispatch(
                new UnbakedTrimsModel(trimmedId(itemId(Items.TRIDENT).getPath()), FLAT),
                conditional(
                        isUsingItem(),
                        new TexturedTridentModelRenderer.UnbakedTrims(getModelLocation(Items.TRIDENT, "_throwing"), trimmedId("trident_entity")),
                        new TexturedTridentModelRenderer.UnbakedTrims(getModelLocation(Items.TRIDENT, "_in_hand"), trimmedId("trident_entity"))
                )
        ));

        register(provider, Items.BOW, conditional(
                isUsingItem(),
                rangeSelect(
                        new UseDuration(false),
                        0.05F,
                        new UnbakedTrimsModel(trimmedId("bow_pulling_0"), getModelLocation(Items.BOW, "_pulling_0")),
                        override(new UnbakedTrimsModel(trimmedId("bow_pulling_1"), getModelLocation(Items.BOW, "_pulling_1")), 0.65F),
                        override(new UnbakedTrimsModel(trimmedId("bow_pulling_2"), getModelLocation(Items.BOW, "_pulling_2")), 0.9F)
                ),
                new UnbakedTrimsModel(trimmedId("bow"), getModelLocation(Items.BOW))
        ));

        register(provider, Items.CROSSBOW, select(
                new Charge(),
                conditional(
                        isUsingItem(),
                        rangeSelect(
                                new CrossbowPull(),
                                new UnbakedTrimsModel(trimmedId("crossbow_pulling_0"), getModelLocation(Items.CROSSBOW, "_pulling_0")),
                                override(new UnbakedTrimsModel(trimmedId("crossbow_pulling_1"), getModelLocation(Items.CROSSBOW, "_pulling_1")), 0.58F),
                                override(new UnbakedTrimsModel(trimmedId("crossbow_pulling_2"), getModelLocation(Items.CROSSBOW, "_pulling_2")), 1.0F)
                        ),
                        new UnbakedTrimsModel(trimmedId("crossbow_standby"), getModelLocation(Items.CROSSBOW))
                ),
                when(CrossbowItem.ChargeType.ARROW, new UnbakedTrimsModel(trimmedId("crossbow_arrow"), getModelLocation(Items.CROSSBOW, "_arrow"))),
                when(CrossbowItem.ChargeType.ROCKET, new UnbakedTrimsModel(trimmedId("crossbow_firework"), getModelLocation(Items.CROSSBOW, "_firework")))
        ));

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
