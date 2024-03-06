package me.srrapero720.embeddium.additions.features.fpsdisplay.placeholders;

import it.unimi.dsi.fastutil.chars.CharArrayList;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;

import java.util.Map;
import java.util.function.Supplier;

public class Placeholder {
    private static final Map<String, Placeholder> PLACEHOLDERS = new Object2ObjectArrayMap<>();

    private String value;
    private long checkpoint = 0;
    private final long msCoolDown;
    private final boolean cacheOnly;
    private final Supplier<String> getter;

    public Placeholder(Supplier<String> getter, long msCoolDown) {
        this.getter = getter;
        this.msCoolDown = msCoolDown;
        this.cacheOnly = this.msCoolDown == -1;
    }

    public String getValue() {
        if (cacheOnly) {
            return this.value == null ? this.value = getter.get() : this.value;
        }
        long ms = Util.getMillis();
        if (checkpoint < ms) {
            checkpoint = ms + this.msCoolDown;
            return this.value = getter.get();
        }
        return this.value;
    }

    public static void register(String key, Placeholder valueSupplier) {
        PLACEHOLDERS.put(key, valueSupplier);
    }

    // TODO: use forge config optimized methods for this
    public static Component parse(String string) {
        boolean watchForKey = false;
        final var nextKey = new CharArrayList(12);
        final var result = new CharArrayList(128);
        final var stringChars = string.toCharArray();

        for (final char c: stringChars) {

            if (c == '{') {
                if (watchForKey) {
                    result.add('{');
                    result.addAll(nextKey);
                    nextKey.clear();
                    continue;
                }

                watchForKey = true;
            } else if (c == '}' && watchForKey) {

                if (nextKey.isEmpty()) {
                    result.add('{');
                    result.add('}');
                    watchForKey = false;
                    continue;
                }

                Placeholder placeholder = PLACEHOLDERS.get(new String(nextKey.toArray(new char[0])));

                if (placeholder == null) {
                    result.add('{');
                    result.addAll(nextKey);
                    result.add('}');
                } else {
                    result.addAll(new CharArrayList(placeholder.getValue().toCharArray()));
                }
                nextKey.clear();
                watchForKey = false;
            } else if (watchForKey) {
                nextKey.add(c);
            } else {
                result.add(c);
            }
        }

        return Component.literal(new String(result.toArray(new char[0])));
    }
}