package com.mcmodders.bakersbounty.client;

import com.mcmodders.bakersbounty.BakersBounty;
import com.mcmodders.bakersbounty.client.renderer.QuernBlockEntityRenderer;
import com.mcmodders.bakersbounty.registry.ModBlockEntities;
import com.mcmodders.bakersbounty.registry.ModBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = BakersBounty.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.QUERN.get(), QuernBlockEntityRenderer::new);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.QUERN.get(), RenderType.cutout());
    }
}