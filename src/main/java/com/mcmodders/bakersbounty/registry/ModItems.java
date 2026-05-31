package com.mcmodders.bakersbounty.registry;

import com.mcmodders.bakersbounty.BakersBounty;
import com.mcmodders.bakersbounty.items.EinkornSeedItem;
import com.mcmodders.bakersbounty.items.GroundStoneItem;
import com.mcmodders.bakersbounty.items.SheafItem;
import com.mcmodders.bakersbounty.items.SickleItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

public class
ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(BakersBounty.MODID);

    public static final DeferredItem<Item> EINKORN_SEEDS = ITEMS.register("einkorn_seeds",
            () -> new EinkornSeedItem(ModBlocks.EINKORN_CROP.get(), new Item.Properties()));


    public static final DeferredItem<Item> COARSE_WHEAT_FLOUR = ITEMS.registerSimpleItem("coarse_wheat_flour");
    public static final DeferredItem<Item> GRITTY_WHEAT_FLOUR = ITEMS.registerSimpleItem("gritty_wheat_flour");

    public static final DeferredItem<Item> QUERN = ITEMS.register("quern",
            () -> new BlockItem(ModBlocks.QUERN.get(), new Item.Properties()));


    public static final DeferredItem<Item> COARSE_FLAT_DOUGH = ITEMS.registerSimpleItem(
            "coarse_flat_dough",
            new Item.Properties());

    public static final DeferredItem<Item> COARSE_FLATBREAD = ITEMS.registerSimpleItem(
            "coarse_flat_bread",
            new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(4.8F).build()));

    public static final DeferredItem<Item> GRITTY_FLAT_DOUGH = ITEMS.registerSimpleItem(
            "gritty_flat_dough",
            new Item.Properties());

    public static final DeferredItem<Item> GRITTY_FLATBREAD = ITEMS.registerSimpleItem(
            "gritty_flat_bread",
            new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(4F).build()));

    public static final DeferredItem<Item> GRITTY_FLOUR_SAC = ITEMS.registerSimpleItem(
            "gritty_flour_sac",
            new Item.Properties());

    public static final DeferredItem<Item> COARSE_FLOUR_SAC = ITEMS.registerSimpleItem(
            "coarse_flour_sac",
            new Item.Properties());

    public static final DeferredItem<Item> GROUND_STONE = ITEMS.register("ground_stone",
            () -> new GroundStoneItem(new Item.Properties().stacksTo(1))
    );

    public static final DeferredItem<Item> WHEAT_SHEAF = ITEMS.register("wheat_sheaf",
            () -> new SheafItem(ModBlocks.WHEAT_STOOK, new Item.Properties()));
    public static final DeferredItem<Item> EINKORN_SHEAF = ITEMS.register("einkorn_sheaf",
            () -> new SheafItem(ModBlocks.EINKORN_STOOK, new Item.Properties()));
    public static final DeferredItem<Item> DRIED_WHEAT_SHEAF = ITEMS.registerSimpleItem("dried_wheat_sheaf");
    public static final DeferredItem<Item> DRIED_EINKORN_SHEAF = ITEMS.registerSimpleItem("dried_einkorn_sheaf");

    public static final DeferredItem<Item> SICKLE = ITEMS.register("sickle",
            () -> new SickleItem(new Item.Properties().durability(64)));

    public static final DeferredItem<Item> WHEAT_STOOK = ITEMS.register("wheat_stook",
            () -> new BlockItem(ModBlocks.WHEAT_STOOK.get(), new Item.Properties()));

    public static final DeferredItem<Item> EINKORN_STOOK = ITEMS.register("einkorn_stook",
            () -> new BlockItem(ModBlocks.EINKORN_STOOK.get(), new Item.Properties()));

    public static final DeferredItem<Item> DRIED_WHEAT_STOOK = ITEMS.register("dried_wheat_stook",
            () -> new BlockItem(ModBlocks.DRIED_WHEAT_STOOK.get(), new Item.Properties()));
    public static final DeferredItem<Item> DRIED_EINKORN_STOOK = ITEMS.register("dried_einkorn_stook",
            () -> new BlockItem(ModBlocks.DRIED_EINKORN_STOOK.get(), new Item.Properties()));


    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BakersBounty.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BAKERS_BOUNTY_TAB = CREATIVE_MODE_TABS.register("bakersbounty_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.bakersbounty"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModItems.EINKORN_SEEDS.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModItems.EINKORN_SEEDS.get());
                output.accept(ModItems.QUERN.get());
                output.accept(ModItems.GROUND_STONE.get());
                output.accept(ModItems.GRITTY_WHEAT_FLOUR.get());
                output.accept(ModItems.COARSE_WHEAT_FLOUR.get());
                output.accept(ModItems.GRITTY_FLOUR_SAC.get());
                output.accept(ModItems.COARSE_FLOUR_SAC.get());
                output.accept(ModItems.GRITTY_FLAT_DOUGH.get());
                output.accept(ModItems.COARSE_FLAT_DOUGH.get());
                output.accept(ModItems.GRITTY_FLATBREAD.get());
                output.accept(ModItems.COARSE_FLATBREAD.get());
                output.accept(ModItems.SICKLE.get());
                output.accept(ModItems.WHEAT_SHEAF.get());
                output.accept(ModItems.EINKORN_SHEAF.get());
                output.accept(ModItems.DRIED_WHEAT_SHEAF.get());
                output.accept(ModItems.DRIED_EINKORN_SHEAF.get());
                output.accept(ModItems.WHEAT_STOOK.get());
                output.accept(ModItems.EINKORN_STOOK.get());
                output.accept(ModItems.DRIED_WHEAT_STOOK.get());
                output.accept(ModItems.DRIED_EINKORN_STOOK.get());
            }).build());

    public static void register(IEventBus eventBus) {
        System.out.println("Registering Baker's Bounty items...");
        ITEMS.register(eventBus);
        System.out.println("Registering Baker's Bounty creative tab...");
        CREATIVE_MODE_TABS.register(eventBus);
        System.out.println("Registration complete!");
    }
}