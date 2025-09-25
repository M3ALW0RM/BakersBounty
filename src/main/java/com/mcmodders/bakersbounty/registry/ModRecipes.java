package com.mcmodders.bakersbounty.registry;

import com.mcmodders.bakersbounty.BakersBounty;
import com.mcmodders.bakersbounty.recipes.QuernRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, BakersBounty.MODID);

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, BakersBounty.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<QuernRecipe>> QUERN_SERIALIZER =
            RECIPE_SERIALIZERS.register("quern_grinding", () -> QuernRecipe.Serializer.INSTANCE);

    public static final DeferredHolder<RecipeType<?>, RecipeType<QuernRecipe>> QUERN_TYPE =
            RECIPE_TYPES.register("quern_grinding", () -> QuernRecipe.Type.INSTANCE);
}