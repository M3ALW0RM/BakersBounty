package com.mcmodders.bakersbounty.datagen;

import com.mcmodders.bakersbounty.BakersBounty;
import com.mcmodders.bakersbounty.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        // Simple wheat flour recipe: 1 cobblestone + 1 wheat seed = 1 wheat flour
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.WHEAT_FLOUR.get())
                .requires(Items.COBBLESTONE)
                .requires(Items.WHEAT_SEEDS)
                .unlockedBy("has_wheat_seeds", has(Items.WHEAT_SEEDS))
                .save(output, BakersBounty.MODID + ":wheat_flour_grinding");
    }
}