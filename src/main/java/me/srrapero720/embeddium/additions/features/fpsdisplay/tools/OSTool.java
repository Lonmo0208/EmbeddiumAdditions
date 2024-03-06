package me.srrapero720.embeddium.additions.features.fpsdisplay.tools;

import me.srrapero720.embeddium.additions.features.fpsdisplay.FPSDisplay;
import me.srrapero720.embeddium.additions.util.EAUtil;

public class OSTool {
    private static final Runtime runtime = Runtime.getRuntime();

    public static String getName() {
        return NAME.name;
    }

    public static String getMem() {
        long memUsage = (getMem$used() * 100L) / runtime.maxMemory();
        return EAUtil.dangerHighValue(memUsage) + String.valueOf(memUsage);
    }

    private static long getMem$used() {
        return runtime.totalMemory() -  runtime.freeMemory();
    }

    public static String getGpu() {
        int gpuUsage = (int) FPSDisplay.minecraft.getGpuUtilization();
        return EAUtil.dangerHighValue(gpuUsage) + String.valueOf(gpuUsage);
    }

    private enum NAME {
        WIN, MAC, NIX, UNK;

        private static final String name;
        static {
            final String OS_NAME = System.getProperty("os.name").toLowerCase();

            NAME OS = OS_NAME.contains("win") ? WIN
                    : OS_NAME.contains("mac") ? MAC
                    : OS_NAME.contains("nux") || OS_NAME.contains("nix") || OS_NAME.contains("freebsd") ? NIX
                    : UNK;

            name = OS.name();
        }
    }
}