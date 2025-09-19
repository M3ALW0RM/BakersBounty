package com.mcmodders.bakersbounty.world.level.block;

import com.mcmodders.bakersbounty.registry.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.ArrayList;
import java.util.List;

public class EinkornBlock extends CropBlock {

    public EinkornBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.EINKORN_SEEDS;
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return new ItemStack(this.getBaseSeedId());
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return blockState.is(Blocks.FARMLAND)       ||
                blockState.is(Blocks.DIRT)          ||
                blockState.is(Blocks.GRASS_BLOCK)   ||
                blockState.is(Blocks.PODZOL)        ||
                blockState.is(Blocks.COARSE_DIRT)   ||
                blockState.is(Blocks.ROOTED_DIRT);
    }
}