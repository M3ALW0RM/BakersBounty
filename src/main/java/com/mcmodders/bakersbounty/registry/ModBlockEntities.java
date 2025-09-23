package com.mcmodders.bakersbounty.registry;

import com.mcmodders.bakersbounty.BakersBounty;
import com.mcmodders.bakersbounty.blockentities.QuernBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, BakersBounty.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<QuernBlockEntity>> QUERN =
            BLOCK_ENTITIES.register("quern", () ->
                    BlockEntityType.Builder.of(QuernBlockEntity::new, ModBlocks.QUERN.get())
                            .build(null));
}