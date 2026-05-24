package com.mcmodders.bakersbounty.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SickleItem extends Item {

    public SickleItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        if (!level.isClientSide() && state.getDestroySpeed(level, pos) != 0.0F) {
            stack.hurtAndBreak(1, miningEntity, EquipmentSlot.MAINHAND);
        }
        return true;
    }
}
