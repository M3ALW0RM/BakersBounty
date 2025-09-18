package com.mcmodders.bakersbounty.loot;

import com.mcmodders.bakersbounty.BakersBounty;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.minecraft.core.registries.BuiltInRegistries;

/**
 * Global Loot Modifier that swaps one item for another in loot tables
 * If the replaced item exists in the loot, it will be swapped with the replacing item
 * Maintains the original stack size during the swap
 */
public class SwapLootModifier extends LootModifier {

    private final Item replacedItem;
    private final Item replacingItem;

    public static final MapCodec<SwapLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            // LootModifier#codecStart adds the conditions field.
            LootModifier.codecStart(inst).and(inst.group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("replaced_item").forGetter(e -> e.replacedItem),
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("replacing_item").forGetter(e -> e.replacingItem)
            )).apply(inst, SwapLootModifier::new)
    );

    public SwapLootModifier(LootItemCondition[] conditions, Item replacedItem, Item replacingItem) {
        super(conditions);
        this.replacedItem = replacedItem;
        this.replacingItem = replacingItem;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {return CODEC;}

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {

        if (context.getQueriedLootTableId().toString().contains("grass")) {
            BakersBounty.LOGGER.debug("Einkorn LootModifier triggered on grass!");
        }
        //else {
        //    return generatedLoot;
        //}
        //generatedLoot.add(new ItemStack(replacingItem));
        // Iterate through the loot and swap matching items
        for (int i = 0; i < generatedLoot.size(); i++) {
            ItemStack stack = generatedLoot.get(i);
            // Check if this stack contains the item to be replaced
            if (stack.is(replacedItem)) {
                // Create new stack with replacing item, maintaining count
                ItemStack newStack = new ItemStack(replacingItem, stack.getCount());
                // Replace the stack in the loot list
                generatedLoot.set(i, newStack);
            }
        }

        return generatedLoot;
    }



    // Getters for debugging/logging purposes
    public Item getReplacedItem() {
        return replacedItem;
    }

    public Item getReplacingItem() {
        return replacingItem;
    }
}