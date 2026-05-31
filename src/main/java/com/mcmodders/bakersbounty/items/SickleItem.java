package com.mcmodders.bakersbounty.items;

import com.mcmodders.bakersbounty.blocks.StookBlock;
import com.mcmodders.bakersbounty.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SickleItem extends Item {

    public SickleItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        if (state.getBlock() instanceof StookBlock
                || state.is(ModBlocks.DRIED_WHEAT_STOOK.get())
                || state.is(ModBlocks.DRIED_EINKORN_STOOK.get())) {
            if (!level.isClientSide) {
                level.destroyBlock(pos, true);
                LivingEntity player = context.getPlayer();
                if (player != null) {
                    context.getItemInHand().hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.getHand()));
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        if (!level.isClientSide() && state.getDestroySpeed(level, pos) != 0.0F) {
            stack.hurtAndBreak(1, miningEntity, EquipmentSlot.MAINHAND);
        }
        return true;
    }
}
