package com.mcmodders.bakersbounty.datagen;

import com.mcmodders.bakersbounty.BakersBounty;
import com.mcmodders.bakersbounty.recipes.QuernRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

public class QuernRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final Item result;
    private final int count;
    private Ingredient ingredient;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    private String group;

    private QuernRecipeBuilder(RecipeCategory category, ItemLike result, int count) {
        this.category = category;
        this.result = result.asItem();
        this.count = count;
    }

    public static QuernRecipeBuilder grinding(RecipeCategory category, ItemLike result) {
        return grinding(category, result, 1);
    }

    public static QuernRecipeBuilder grinding(RecipeCategory category, ItemLike result, int count) {
        return new QuernRecipeBuilder(category, result, count);
    }

    public QuernRecipeBuilder requires(ItemLike input) {
        return this.requires(Ingredient.of(input));
    }

    public QuernRecipeBuilder requires(Ingredient ingredient) {
        this.ingredient = ingredient;
        return this;
    }

    @Override
    public QuernRecipeBuilder unlockedBy(String criterionName, Criterion<?> criterion) {
        this.criteria.put(criterionName, criterion);
        return this;
    }

    @Override
    public QuernRecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getResult() {
        return this.result;
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        if (this.ingredient == null) {
            throw new IllegalStateException("No ingredient defined for quern recipe");
        }

        this.ensureValid(id);
        Advancement.Builder advancement = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);

        this.criteria.forEach(advancement::addCriterion);

        QuernRecipe recipe = new QuernRecipe(this.ingredient, new ItemStack(this.result, this.count));

        recipeOutput.accept(id, recipe, advancement.build(id.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private void ensureValid(ResourceLocation id) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

    public void save(RecipeOutput recipeOutput, String recipeName) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(BakersBounty.MODID, recipeName);
        this.save(recipeOutput, resourceLocation);
    }
}