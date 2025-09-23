package com.mcmodders.bakersbounty.registry;

import com.mcmodders.bakersbounty.BakersBounty;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.common.NeoForge;

import java.util.List;

public class
ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(BakersBounty.MODID);

    public static final DeferredItem<Item> EINKORN_SEEDS = ITEMS.register("einkorn_seeds",
            () -> new ItemNameBlockItem(ModBlocks.EINKORN_CROP.get(), new Item.Properties()));

    // Add wheat flour item
    public static final DeferredItem<Item> WHEAT_FLOUR = ITEMS.registerSimpleItem(
            "wheat_flour",
            new Item.Properties());


    // Add wheat flour item
    public static final DeferredItem<Item> BREAD = ITEMS.registerSimpleItem(
            "bread",
            new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(3).build()));

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BakersBounty.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BAKERS_BOUNTY_TAB = CREATIVE_MODE_TABS.register("bakersbounty_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.bakersbounty"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModItems.EINKORN_SEEDS.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModItems.EINKORN_SEEDS.get());
                output.accept(ModItems.WHEAT_FLOUR.get());
            }).build());

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        CREATIVE_MODE_TABS.register(eventBus);
    }
}