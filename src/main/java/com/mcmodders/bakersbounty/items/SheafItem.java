package com.mcmodders.bakersbounty.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class SheafItem extends Item {

    private final Supplier<Block> stookBlock;

    public SheafItem(Supplier<Block> stookBlock, Properties properties) {
        super(properties);
        this.stookBlock = stookBlock;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        if (stack.getCount() < 4) return InteractionResult.PASS;
        if (context.getClickedFace() != Direction.UP) return InteractionResult.PASS;

        Level level = context.getLevel();
        BlockPos placePos = context.getClickedPos().above();
        BlockState existing = level.getBlockState(placePos);
        if (!existing.canBeReplaced()) return InteractionResult.PASS;

        if (!level.isClientSide()) {
            Player player = context.getPlayer();
            level.setBlock(placePos, stookBlock.get().defaultBlockState(), 3);
            if (player != null && !player.isCreative()) {
                stack.shrink(4);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
