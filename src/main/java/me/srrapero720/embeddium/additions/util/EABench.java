package me.srrapero720.embeddium.additions.util;

import it.unimi.dsi.fastutil.longs.LongLongMutablePair;
import it.unimi.dsi.fastutil.longs.LongLongPair;
import net.minecraft.Util;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import static me.srrapero720.embeddium.additions.EmbeddiumAdditions.LOGGER;

public class EABench {
    private final LongLongPair[] times = new LongLongPair[100];
    private int pos = 0;
    private final Marker it;
    public EABench(String name) {
        this.it = MarkerManager.getMarker(name);
    }

    public void start() {
        times[pos] = new LongLongMutablePair(Util.getNanos(), -1);
    }

    public void end() {
        times[pos].right(Util.getNanos());
        if (pos++ != times.length - 1) return;

        long totalStart = 0;
        long totalEnd = 0;
        long highTime = 0;

        for (LongLongPair t: times) {
            long start = t.firstLong();
            long end = t.secondLong();

            long time = end - start;
            if (time > highTime) highTime = time;

            totalStart += start;
            totalEnd += end;
        }

        long totalTime = totalEnd - totalStart;
        long totalAVGTime = totalTime / times.length;

        LOGGER.info(it, "({}) BENCH TIME FINISHEDx{}: AVG Time {}ns/{}ms - MAX Time {}ns/{}ms", it.getName(), times.length - 1, totalAVGTime, totalAVGTime / 1000000L, highTime, highTime / 1000000L);
        pos = 0;
    }
}
