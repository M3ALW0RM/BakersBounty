package com.mcmodders.bakersbounty.registry;

import com.mcmodders.bakersbounty.BakersBounty;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.common.NeoForge;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(BakersBounty.MODID);
    public static final DeferredItem<Item> EINKORN_SEEDS =
            ITEMS.register("einkorn_seeds",
                    () -> new ItemNameBlockItem(ModBlocks.EINKORN_CROP.get(),
                            new Item.Properties()));

    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "bakersbounty" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BakersBounty.MODID);

    // Creates a creative tab with the id "bakersbounty:example_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BAKERS_BOUNTY_TAB = CREATIVE_MODE_TABS.register("bakersbounty_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.bakersbounty")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModItems.EINKORN_SEEDS.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModItems.EINKORN_SEEDS.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());

    public ModItems(IEventBus modEventBus, ModContainer modContainer){
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}