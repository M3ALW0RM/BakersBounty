package com.mcmodders.bakersbounty.items;

import com.mcmodders.bakersbounty.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class EinkornSeedItem extends ItemNameBlockItem {

    private static final float WHEAT_MUTATION_CHANCE = 1.0F / 8.0F;

    public EinkornSeedItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionResult result = super.useOn(context);

        Level level = context.getLevel();
        if (!level.isClientSide && result.consumesAction()) {
            if (level.getRandom().nextFloat() < WHEAT_MUTATION_CHANCE) {
                BlockPos plantPos = context.getClickedPos().relative(context.getClickedFace());
                BlockState plantedState = level.getBlockState(plantPos);

                if (plantedState.is(ModBlocks.EINKORN_CROP.get())) {
                    BlockState wheatState = Blocks.WHEAT.defaultBlockState();
                    if (wheatState.canSurvive(level, plantPos)) {
                        level.setBlock(plantPos, wheatState, 3);
                    }
                }
            }
        }

        return result;
    }
}
