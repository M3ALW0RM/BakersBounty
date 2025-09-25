package com.mcmodders.bakersbounty.blockentities;

import com.mcmodders.bakersbounty.registry.ModBlockEntities;
import com.mcmodders.bakersbounty.recipes.QuernRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class QuernBlockEntity extends BlockEntity {
    private ItemStack inputItem = ItemStack.EMPTY;
    private ItemStack outputItem = ItemStack.EMPTY;
    private int grindingProgress = 0;
    private int maxGrindingProgress = 3; // Number of right-clicks needed

    public QuernBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.QUERN.get(), pos, blockState);
    }

    public boolean canAcceptItem(ItemStack item) {
        if (!inputItem.isEmpty()) {
            return false; // Already has an item
        }

        // Check if there's a recipe for this item
        if (level != null) {
            SingleRecipeInput recipeInput = new SingleRecipeInput(item);
            Optional<RecipeHolder<QuernRecipe>> recipe = level.getRecipeManager()
                    .getRecipeFor(QuernRecipe.Type.INSTANCE, recipeInput, level);
            return recipe.isPresent();
        }
        return false;
    }

    public boolean insertItem(ItemStack item) {
        if (canAcceptItem(item) && inputItem.isEmpty()) {
            inputItem = item.copy();
            inputItem.setCount(1);
            grindingProgress = 0;
            setChanged();
            if (level != null) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
            return true;
        }
        return false;
    }

    public void grind() {
        if (!inputItem.isEmpty() && outputItem.isEmpty()) {
            grindingProgress++;

            if (grindingProgress >= maxGrindingProgress) {
                // Complete the grinding process
                if (level != null) {
                    SingleRecipeInput recipeInput = new SingleRecipeInput(inputItem);
                    Optional<RecipeHolder<QuernRecipe>> recipeHolder = level.getRecipeManager()
                            .getRecipeFor(QuernRecipe.Type.INSTANCE, recipeInput, level);

                    if (recipeHolder.isPresent()) {
                        QuernRecipe recipe = recipeHolder.get().value();
                        outputItem = recipe.getResultItem(level.registryAccess()).copy();
                        inputItem = ItemStack.EMPTY;
                        grindingProgress = 0;
                    }
                }
            }

            setChanged();
            if (level != null) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    }

    public ItemStack extractOutput() {
        if (!outputItem.isEmpty()) {
            ItemStack result = outputItem.copy();
            outputItem = ItemStack.EMPTY;
            setChanged();
            if (level != null) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
            return result;
        }
        return ItemStack.EMPTY;
    }

    public boolean hasInputItem() {
        return !inputItem.isEmpty();
    }

    public boolean hasOutputItem() {
        return !outputItem.isEmpty();
    }

    public ItemStack getInputItem() {
        return inputItem;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public int getGrindingProgress() {
        return grindingProgress;
    }

    public int getMaxGrindingProgress() {
        return maxGrindingProgress;
    }

    public float getGrindingProgressPercent() {
        return maxGrindingProgress == 0 ? 0 : (float) grindingProgress / maxGrindingProgress;
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(2);
        inventory.setItem(0, inputItem);
        inventory.setItem(1, outputItem);

        if (level != null) {
            Containers.dropContents(level, worldPosition, inventory);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("InputItem", inputItem.saveOptional(registries));
        tag.put("OutputItem", outputItem.saveOptional(registries));
        tag.putInt("GrindingProgress", grindingProgress);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inputItem = ItemStack.parseOptional(registries, tag.getCompound("InputItem"));
        outputItem = ItemStack.parseOptional(registries, tag.getCompound("OutputItem"));
        grindingProgress = tag.getInt("GrindingProgress");
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

    // Static tick method for future use if needed
    public static void tick(Level level, BlockPos pos, BlockState state, QuernBlockEntity blockEntity) {
        // Add any tick-based logic here if needed
    }
}