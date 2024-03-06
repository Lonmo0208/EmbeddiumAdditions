package me.srrapero720.embeddium.additions.features.fpsdisplay.tools;

import me.srrapero720.embeddium.additions.util.EAUtil;
import net.minecraft.client.Minecraft;

public class FPSTool {
    public static String getFPS() {
        int fps = Minecraft.getInstance().getFps();
        return EAUtil.dangerLowValue(fps) + String.valueOf(fps);
    }

    public static String getAvgFPS() {
        int avg = AVG.calculate();
        return EAUtil.dangerLowValue(avg) + String.valueOf(avg);
    }

    public static String getMinFPS() {
        int min = MIN.calculate();
        return EAUtil.dangerLowValue(min) + String.valueOf(min);
    }

    private static class AVG {
        private static final int[] values = new int[14];
        private static int index = 0;

        public static int calculate() {
            int currentFPS = Minecraft.getInstance().getFps();
            if (index == values.length) index = 0;

            if (currentFPS != values[index]) {
                values[index] = currentFPS;
                index++;
            }

            int totalFPS = 0;
            int i = 0;
            while (i < values.length) totalFPS += values[i++];

            return totalFPS / values.length;
        }
    }

    private static class MIN {
        private static int minFPS = 0;

        public static int calculate() {
            var minecraft = Minecraft.getInstance();

            final var timer = minecraft.getFrameTimer();
            final int fps = Math.max(minecraft.getFps(), 1);

            final int start = timer.getLogStart();
            final int end = timer.getLogEnd();
            final long[] logs = timer.getLog();

            if (end == start) return minFPS;

            long maxNS = (long) (1 / (double) fps * 1000000000);
            long totalNS = 0;

            int index = Math.floorMod(end - 1, logs.length);
            while (index != start && (double) totalNS < 1000000000) {
                long timeNs = logs[index];
                maxNS = Math.max(maxNS, timeNs);
                totalNS += timeNs;
                index = Math.floorMod(index - 1, logs.length);
            }

            return minFPS = (int) (1 / ((double) maxNS / 1000000000));
        }
    }
}