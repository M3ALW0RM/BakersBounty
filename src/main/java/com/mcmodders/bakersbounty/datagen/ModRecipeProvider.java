package com.mcmodders.bakersbounty.datagen;

import com.mcmodders.bakersbounty.BakersBounty;
import com.mcmodders.bakersbounty.registry.ModBlocks;
import com.mcmodders.bakersbounty.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

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
				
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.QUERN.get())
                .pattern(" S#")
                .pattern(" S ")
                .pattern("WWW")
                .define('S', Items.SMOOTH_STONE_SLAB)
                .define('W', ItemTags.WOODEN_SLABS)
                .define('#', Items.STICK)
                .unlockedBy("has_stone_slab", has(Items.STONE_SLAB))
                .unlockedBy("has_stone_slab", has(ItemTags.WOODEN_SLABS))
                .save(output);

        QuernRecipeBuilder.grinding(RecipeCategory.FOOD, ModItems.WHEAT_FLOUR.get())
                .requires(Items.WHEAT_SEEDS)
                .unlockedBy("has_wheat_seeds", has(Items.WHEAT_SEEDS))
                .unlockedBy("has_quern", has(ModBlocks.QUERN.get()))
                .save(output, "wheat_seeds_to_flour");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.FLOUR_SAC.get(), 1)
                .requires(ModItems.WHEAT_FLOUR, 9)
                .unlockedBy("has_wheat_flour", has(ModItems.WHEAT_FLOUR))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.COARSE_FLATBREAD.get(), 3)
                .requires(ModItems.FLOUR_SAC)
                .requires(Items.WATER_BUCKET)
                .unlockedBy("has_flour_sac", has(ModItems.FLOUR_SAC))
                .save(output);
    }
}