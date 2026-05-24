package com.mcmodders.bakersbounty.loot;

import com.mcmodders.bakersbounty.BakersBounty;
import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Supplier;

/**
 * Registry for Global Loot Modifiers
 * Handles registration of both Swap and SwapOrAdd modifiers
 */
public class ModLootModifiers {

    // Create deferred register for loot modifier serializers
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIER_SERIALIZERS  =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, BakersBounty.MODID);

    public static final Supplier<MapCodec<SwapLootModifier>> SWAP =
           GLOBAL_LOOT_MODIFIER_SERIALIZERS.register("swap", () -> SwapLootModifier.CODEC);

    public static final Supplier<MapCodec<SickleLootModifier>> SICKLE_HARVEST =
            GLOBAL_LOOT_MODIFIER_SERIALIZERS.register("sickle_harvest", () -> SickleLootModifier.CODEC);

    // Register the SwapOrAdd modifier serializer
    //public static final Supplier<Codec<SwapOrAddLootModifier>> SWAP_OR_ADD =
    //        LOOT_MODIFIERS.register("swap_or_add", () -> SwapOrAddLootModifier.Serializer.CODEC);

    /**
     * Register all loot modifiers
     * Call this in your mod's constructor
     */
    public static void register(IEventBus modEventBus) {
        GLOBAL_LOOT_MODIFIER_SERIALIZERS.register(modEventBus);
    }
}