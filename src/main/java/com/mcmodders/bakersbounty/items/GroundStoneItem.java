package com.mcmodders.bakersbounty.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class GroundStoneItem extends Item {

    public GroundStoneItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        ItemStack result = itemStack.copy();
        result.setDamageValue(0); // Reset any damage if using durability
        return result;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }
}