package com.mcmodders.bakersbounty.blocks;

import com.mcmodders.bakersbounty.blockentities.StookBlockEntity;
import com.mcmodders.bakersbounty.registry.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class StookBlock extends BaseEntityBlock {

    public static final MapCodec<StookBlock> CODEC = simpleCodec(StookBlock::new);

    public StookBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StookBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) return null;
        return createTickerHelper(blockEntityType, ModBlockEntities.STOOK.get(), StookBlockEntity::serverTick);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        // Model spans x/z [2,14] and y [0,19] in pixels — drip from a random side face
        double y = pos.getY() + random.nextDouble() * (19.0 / 16.0);
        double x, z;
        if (random.nextBoolean()) {
            x = pos.getX() + 0.125 + random.nextDouble() * 0.75;
            z = pos.getZ() + (random.nextBoolean() ? 0.125 : 0.875);
        } else {
            x = pos.getX() + (random.nextBoolean() ? 0.125 : 0.875);
            z = pos.getZ() + 0.125 + random.nextDouble() * 0.75;
        }
        level.addParticle(ParticleTypes.DRIPPING_WATER, x, y, z, 0, 0, 0);
    }
}
