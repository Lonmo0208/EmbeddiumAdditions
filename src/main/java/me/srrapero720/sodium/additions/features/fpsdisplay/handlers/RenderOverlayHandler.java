package me.srrapero720.sodium.additions.features.fpsdisplay.handlers;

import me.srrapero720.sodium.additions.SodiumAdditions;
import me.srrapero720.sodium.additions.features.fpsdisplay.FPSDisplay;
import me.srrapero720.sodium.additions.util.EAConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SodiumAdditions.ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class RenderOverlayHandler {

    // THIS IS A STUPID FIX... IS SIMPLE AND WORKS
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRenderOverlayPre(RenderGuiOverlayEvent.Pre event) {
        boolean cancelOverlayRendering = !event.getOverlay().id().getPath().equals("debug_text")
                && Minecraft.getInstance().options.renderFpsChart;

        if (cancelOverlayRendering) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onRenderOverlayPost(RenderGuiOverlayEvent.Post event) {
        FPSDisplay.render(EAConfig.getFpsText(), event.getWindow().getGuiScale(), event.getGuiGraphics(), Minecraft.getInstance().font);
    }
}