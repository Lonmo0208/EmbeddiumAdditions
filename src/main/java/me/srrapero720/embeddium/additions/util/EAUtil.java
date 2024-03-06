package me.srrapero720.embeddium.additions.util;

import net.minecraft.ChatFormatting;

public final class EAUtil {

    public static ChatFormatting dangerLowValue(final int v) {
        return v <= 10 ? ChatFormatting.RED
                : v <= 20 ? ChatFormatting.GOLD
                : v <= 40 ? ChatFormatting.YELLOW
                : ChatFormatting.RESET
                ;
    }

    public static ChatFormatting dangerHighValue(final int v) {
        return v >= 100 ? ChatFormatting.RED
                : v >= 90 ? ChatFormatting.GOLD
                : v >= 75 ? ChatFormatting.YELLOW
                : ChatFormatting.RESET
                ;
    }

    public static ChatFormatting dangerHighValue(final double v) {
        return dangerHighValue((int) v);
    }
}