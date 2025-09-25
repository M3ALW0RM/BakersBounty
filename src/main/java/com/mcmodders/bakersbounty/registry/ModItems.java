package com.mcmodders.bakersbounty.registry;

import com.mcmodders.bakersbounty.BakersBounty;
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
            () -> new ItemNameBlockItem(ModBlocks.EINKORN_CROP.get(), new Item.Properties()));

    // Add wheat flour item
    public static final DeferredItem<Item> WHEAT_FLOUR = ITEMS.registerSimpleItem("wheat_flour");

    public static final DeferredItem<Item> QUERN = ITEMS.register("quern",
            () -> new BlockItem(ModBlocks.QUERN.get(), new Item.Properties()));


    // Add wheat flour item
    public static final DeferredItem<Item> COARSE_FLATBREAD = ITEMS.registerSimpleItem(
            "coarse_flatbread",
            new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(4.8F).build()));

    public static final DeferredItem<Item> FLOUR_SAC = ITEMS.registerSimpleItem(
            "flour_sac",
            new Item.Properties());


    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BakersBounty.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BAKERS_BOUNTY_TAB = CREATIVE_MODE_TABS.register("bakersbounty_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.bakersbounty"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModItems.EINKORN_SEEDS.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModItems.EINKORN_SEEDS.get());
                output.accept(ModItems.WHEAT_FLOUR.get());
                output.accept(ModItems.QUERN.get());
                output.accept(ModItems.COARSE_FLATBREAD.get());
                output.accept(ModItems.FLOUR_SAC.get());
            }).build());

    public static void register(IEventBus eventBus) {
        System.out.println("Registering Baker's Bounty items...");
        ITEMS.register(eventBus);
        System.out.println("Registering Baker's Bounty creative tab...");
        CREATIVE_MODE_TABS.register(eventBus);
        System.out.println("Registration complete!");
    }
}