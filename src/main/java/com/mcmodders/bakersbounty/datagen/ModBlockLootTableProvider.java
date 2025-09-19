package com.mcmodders.bakersbounty.datagen;

import com.mcmodders.bakersbounty.registry.ModBlocks;
import com.mcmodders.bakersbounty.registry.ModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {

    private final HolderLookup.Provider registries;

    public ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
        this.registries = registries;
    }

    @Override
    protected void generate() {
        // Custom einkorn crop loot table with your requirements
        this.add(ModBlocks.EINKORN_CROP.get(),
                LootTable.lootTable()
                        // Pool 1: Einkorn seeds when fully grown (2-3 seeds)
                        .withPool(applyExplosionCondition(ModBlocks.EINKORN_CROP.get(),
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1.0F))
                                        .add(LootItem.lootTableItem(ModItems.EINKORN_SEEDS.get())
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F))))
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.EINKORN_CROP.get())
                                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, 7)))
                        ))
                        // Pool 2: Wheat seeds when fully grown (12.5% chance = 1 in 8)
                        .withPool(applyExplosionCondition(ModBlocks.EINKORN_CROP.get(),
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1.0F))
                                        .add(LootItem.lootTableItem(Items.WHEAT_SEEDS))
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.EINKORN_CROP.get())
                                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, 7)))
                                        .when(LootItemRandomChanceCondition.randomChance(0.125F)) // 12.5% = 1/8 chance
                        ))
                        // Pool 3: Einkorn seeds when not fully grown (1 seed for replanting)
                        .withPool(applyExplosionCondition(ModBlocks.EINKORN_CROP.get(),
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1.0F))
                                        .add(LootItem.lootTableItem(ModItems.EINKORN_SEEDS.get()))
                                        .when(InvertedLootItemCondition.invert(
                                                LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.EINKORN_CROP.get())
                                                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, 7))
                                        )) // NOT age 7 (so ages 0-6)
                        ))
        );
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream()
                .map(entry -> (Block) entry.value())::iterator;
    }
}