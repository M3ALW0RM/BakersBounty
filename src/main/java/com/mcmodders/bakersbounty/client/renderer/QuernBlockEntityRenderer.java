package com.mcmodders.bakersbounty.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mcmodders.bakersbounty.blockentities.QuernBlockEntity;
import com.mcmodders.bakersbounty.blocks.QuernBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class QuernBlockEntityRenderer implements BlockEntityRenderer<QuernBlockEntity> {
    private final ItemRenderer itemRenderer;

    public QuernBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(QuernBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {

        ItemStack inputItem = blockEntity.getInputItem();
        ItemStack outputItem = blockEntity.getOutputItem();
        Direction facing = blockEntity.getBlockState().getValue(QuernBlock.FACING);

        // Render input item on top of the quern
        if (!inputItem.isEmpty()) {
            renderInputItem(blockEntity, inputItem, poseStack, buffer, packedLight, packedOverlay, partialTick);
        }

        // Render output item at the front of the quern
        if (!outputItem.isEmpty()) {
            renderOutputItem(outputItem, poseStack, buffer, packedLight, packedOverlay, facing);
        }
    }

    private void renderInputItem(QuernBlockEntity blockEntity, ItemStack item, PoseStack poseStack,
                                 MultiBufferSource buffer, int packedLight, int packedOverlay, float partialTick) {
        poseStack.pushPose();

        // Position the item on top of the quern, slightly elevated
        poseStack.translate(0.5D, 0.5D, 0.4D);

        // Scale the item down a bit
        poseStack.scale(0.5F, 0.5F, 0.5F);

        // Rotate the item to lay flat
        poseStack.mulPose(Axis.XP.rotationDegrees(90));

        // Add a slight rotation based on grinding progress for visual feedback
        float progress = blockEntity.getGrindingProgressPercent();
        float rotationAngle = progress * 90F + 180F;

        // Add time-based animation for active grinding
        if (progress > 0 && progress < 1.0F) {
            // Add a subtle oscillation when grinding is in progress
            long time = blockEntity.getLevel() != null ? blockEntity.getLevel().getGameTime() : 0;
            float timeRotation = (float) Math.sin((time + partialTick) * 0.5F) * 5F;
            rotationAngle += timeRotation;
        }

        poseStack.mulPose(Axis.ZP.rotationDegrees(rotationAngle));

        itemRenderer.renderStatic(item, ItemDisplayContext.GROUND,
                packedLight, packedOverlay, poseStack, buffer,
                blockEntity.getLevel(), 0);

        poseStack.popPose();
    }

    private void renderOutputItem(ItemStack item, PoseStack poseStack, MultiBufferSource buffer,
                                  int packedLight, int packedOverlay, Direction facing) {
        poseStack.pushPose();

        // Position the item at the front of the quern based on facing direction
        double offsetX = 0.5D;
        double offsetZ = -0.5D;

        switch (facing) {
            case NORTH -> offsetZ = -0.5D;
            case SOUTH -> offsetZ = 0.8D;
            case WEST -> offsetX = 0.2D;
            case EAST -> offsetX = 0.8D;
        }
        //poseStack.translate(0.5D, 1.0D, 0.5D);
        poseStack.translate(offsetX, 0.1D, offsetZ);

        // Scale the item down
        poseStack.scale(0.375F, 0.375F, 0.375F);

        // Rotate based on facing direction
        switch (facing) {
            case NORTH -> poseStack.mulPose(Axis.YP.rotationDegrees(0));
            case SOUTH -> poseStack.mulPose(Axis.YP.rotationDegrees(180));
            case WEST -> poseStack.mulPose(Axis.YP.rotationDegrees(270));
            case EAST -> poseStack.mulPose(Axis.YP.rotationDegrees(90));
        }

        // Use null for level parameter since output items don't need world context
        itemRenderer.renderStatic(item, ItemDisplayContext.GROUND,
                packedLight, packedOverlay, poseStack, buffer,
                null, 0);

        poseStack.popPose();
    }
}