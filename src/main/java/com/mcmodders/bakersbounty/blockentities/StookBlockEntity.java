package com.mcmodders.bakersbounty.blockentities;

import com.mcmodders.bakersbounty.registry.ModBlockEntities;
import com.mcmodders.bakersbounty.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class StookBlockEntity extends BlockEntity {

    private static final int DRYING_TICKS_REQUIRED = 200;
    private int dryingProgress = 0;

    public StookBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.STOOK.get(), pos, blockState);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, StookBlockEntity blockEntity) {
        blockEntity.dryingProgress++;
        blockEntity.setChanged();
        if (blockEntity.dryingProgress >= DRYING_TICKS_REQUIRED) {
            BlockState driedState = state.is(ModBlocks.WHEAT_STOOK.get())
                    ? ModBlocks.DRIED_WHEAT_STOOK.get().defaultBlockState()
                    : ModBlocks.DRIED_EINKORN_STOOK.get().defaultBlockState();
            level.setBlock(pos, driedState, 3);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("DryingProgress", dryingProgress);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        dryingProgress = tag.getInt("DryingProgress");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
