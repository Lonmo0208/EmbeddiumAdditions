package me.srrapero720.embeddium.additions.features.fpsdisplay;

import me.srrapero720.embeddium.additions.features.fpsdisplay.placeholders.Placeholder;
import me.srrapero720.embeddium.additions.features.fpsdisplay.tools.FPSTool;
import me.srrapero720.embeddium.additions.features.fpsdisplay.tools.OSTool;
import me.srrapero720.embeddium.additions.util.EAConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class FPSDisplay {
    public static final Minecraft minecraft = Minecraft.getInstance();

    private static final MutableComponent TXT_FPS = Component.translatable("embeddium.additions.fps_display.fps");
    private static final MutableComponent TXT_MIN = Component.translatable("embeddium.additions.fps_display.min");
    private static final MutableComponent TXT_AVG = Component.translatable("embeddium.additions.fps_display.avg");
    private static final MutableComponent TXT_OS = Component.translatable("embeddium.additions.fps_display.os");
    private static final MutableComponent TXT_GPU = Component.translatable("embeddium.additions.fps_display.gpu");
    private static final MutableComponent TXT_MEM = Component.translatable("embeddium.additions.fps_display.mem");

    static {
        Placeholder.register("fps", new Placeholder(FPSTool::getFPS, 0));
        Placeholder.register("fps.avg", new Placeholder(FPSTool::getAvgFPS, 0));
        Placeholder.register("fps.min", new Placeholder(FPSTool::getMinFPS, 500));
        Placeholder.register("os.name", new Placeholder(OSTool::getName, -1));
        Placeholder.register("os.mem", new Placeholder(OSTool::getMem, 500));
        Placeholder.register("os.gpu", new Placeholder(OSTool::getGpu, 1000));

        Placeholder.register("text.fps", new Placeholder(TXT_FPS::getString, 5000));
        Placeholder.register("text.min", new Placeholder(TXT_MIN::getString, 5000));
        Placeholder.register("text.avg", new Placeholder(TXT_AVG::getString, 5000));
        Placeholder.register("text.os", new Placeholder(TXT_OS::getString, 5000));
        Placeholder.register("text.mem", new Placeholder(TXT_MEM::getString, 5000));
        Placeholder.register("text.gpu", new Placeholder(TXT_GPU::getString, 5000));
    }

    public static void render(String baseStr, double scale, GuiGraphics graphics, Font font) {
        if (baseStr == null || minecraft.options.renderDebug || minecraft.options.renderFpsChart) return;

        Component text = Placeholder.parse(baseStr);

        // TODO: use forge config optimized methods for this
        final int xMargin = getScaledMargin(scale, EAConfig.fpsTextHorizontalMargin.get());
        final int yMargin = getScaledMargin(scale, EAConfig.fpsTextVerticalMargin.get());

        final int y = yMargin;
        final int x = switch (EAConfig.fpsTextGravity.get()) {
            case LEFT -> xMargin;
            case CENTER -> (graphics.guiWidth() - font.width(text) / 2);
            case RIGHT -> (graphics.guiWidth() - font.width(text)) - xMargin;
        };

        graphics.pose().pushPose();
        if (EAConfig.fpsTextboxShadow.get()) {
            graphics.fill(x - 2, y - 2, x + font.width(text) + 2, y + font.lineHeight + 1, 0x6FAFAFB0);
            graphics.flush();
        }

        graphics.drawString(font, text, x, y, 0xffffffff, true);
        graphics.pose().popPose();
    }

    private static int getScaledMargin(double scale, int margin) {
        return (scale > 0) ? (int) (margin / scale) : margin;
    }
}