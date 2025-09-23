package com.mcmodders.bakersbounty.recipes;

import com.mcmodders.bakersbounty.BakersBounty;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class QuernRecipe implements Recipe<SingleRecipeInput> {
    private final Ingredient input;
    private final ItemStack output;

    public QuernRecipe(Ingredient input, ItemStack output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean matches(SingleRecipeInput container, Level level) {
        if (level.isClientSide()) {
            return false;
        }
        return input.test(container.item());
    }

    @Override
    public ItemStack assemble(SingleRecipeInput container, HolderLookup.Provider registries) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return output.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public Ingredient getInput() {
        return input;
    }

    public static class Type implements RecipeType<QuernRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "quern_grinding";

        private Type() {}
    }

    public static class Serializer implements RecipeSerializer<QuernRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(BakersBounty.MODID, "quern_grinding");

        public static final MapCodec<QuernRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.input),
                        ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.output)
                ).apply(instance, QuernRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, QuernRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.input,
                ItemStack.STREAM_CODEC, recipe -> recipe.output,
                QuernRecipe::new
        );

        @Override
        public MapCodec<QuernRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, QuernRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}