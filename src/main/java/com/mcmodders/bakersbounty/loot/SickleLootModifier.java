package com.mcmodders.bakersbounty.loot;

import com.mcmodders.bakersbounty.items.SickleItem;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class SickleLootModifier extends LootModifier {

    private final Item sheafItem;

    public static final MapCodec<SickleLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            LootModifier.codecStart(inst).and(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("sheaf_item").forGetter(e -> e.sheafItem)
            ).apply(inst, SickleLootModifier::new)
    );

    public SickleLootModifier(LootItemCondition[] conditions, Item sheafItem) {
        super(conditions);
        this.sheafItem = sheafItem;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        if (tool == null || !(tool.getItem() instanceof SickleItem)) {
            return generatedLoot;
        }
        generatedLoot.clear();
        generatedLoot.add(new ItemStack(sheafItem, 1));
        return generatedLoot;
    }
}
