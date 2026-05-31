package com.mcmodders.bakersbounty.registry;

import com.mcmodders.bakersbounty.BakersBounty;
import com.mcmodders.bakersbounty.blocks.QuernBlock;
import com.mcmodders.bakersbounty.blocks.StookBlock;
import com.mcmodders.bakersbounty.world.level.block.EinkornBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(BakersBounty.MODID);

    public static final DeferredBlock<Block> EINKORN_CROP =
            BLOCKS.register("einkorn_crop",
                    ()  -> new EinkornBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT)));

    public static final DeferredHolder<Block, QuernBlock> QUERN = BLOCKS.register("quern",
            () -> new QuernBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(3.5f)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()));

    public static final DeferredBlock<Block> WHEAT_STOOK = BLOCKS.register("wheat_stook",
            () -> new StookBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .strength(0.8f)
                    .ignitedByLava()
                    .noOcclusion()));

    public static final DeferredBlock<Block> EINKORN_STOOK = BLOCKS.register("einkorn_stook",
            () -> new StookBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .strength(0.8f)
                    .ignitedByLava()
                    .noOcclusion()));

    public static final DeferredBlock<Block> DRIED_WHEAT_STOOK = BLOCKS.register("dried_wheat_stook",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(0.8f)
                    .ignitedByLava()
                    .noOcclusion()));

    public static final DeferredBlock<Block> DRIED_EINKORN_STOOK = BLOCKS.register("dried_einkorn_stook",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(0.8f)
                    .ignitedByLava()
                    .noOcclusion()));

    // This method must be called during mod initialization!
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
