package com.mcmodders.bakersbounty.blocks;

import com.mcmodders.bakersbounty.blockentities.QuernBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import com.mojang.serialization.MapCodec;

public class QuernBlock extends BaseEntityBlock {
    public static final MapCodec<QuernBlock> CODEC = simpleCodec(QuernBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    // Define the shape of the quern block (adjust as needed)
    private static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D);

    public QuernBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true; // Allows proper transparency handling
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true; // Allows skylight through transparent areas
    }

    // And this one for proper light calculation
    @Override
    public int getLightBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return 0; // No light blocking for transparent parts
    }

    // This helps with transparency rendering
    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty(); // For proper transparency rendering
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof QuernBlockEntity quernBlockEntity) {
                ItemStack heldItem = player.getMainHandItem();

                if (!heldItem.isEmpty() && quernBlockEntity.canAcceptItem(heldItem)) {
                    // Try to insert item
                    if (quernBlockEntity.hasOutputItem()) {
                        ItemStack outputItem = quernBlockEntity.extractOutput();
                        if (!outputItem.isEmpty()) {
                            player.getInventory().add(outputItem);
                        }
                        if (quernBlockEntity.insertItem(heldItem)) {
                            if (!player.getAbilities().instabuild) {
                                heldItem.shrink(1);
                            }
                        }
                        return InteractionResult.SUCCESS;
                    }
                    else if (quernBlockEntity.insertItem(heldItem)) {
                        if (!player.getAbilities().instabuild) {
                            heldItem.shrink(1);
                        }
                        return InteractionResult.SUCCESS;
                    }
                } else if (true){//heldItem.isEmpty()) {
                    // Try to grind or extract
                    if (quernBlockEntity.hasInputItem()) {
                        quernBlockEntity.grind();
                        return InteractionResult.SUCCESS;
                    } else if (quernBlockEntity.hasOutputItem()) {
                        ItemStack outputItem = quernBlockEntity.extractOutput();
                        if (!outputItem.isEmpty()) {
                            player.getInventory().add(outputItem);
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new QuernBlockEntity(pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof QuernBlockEntity quernBlockEntity) {
                quernBlockEntity.drops();
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}