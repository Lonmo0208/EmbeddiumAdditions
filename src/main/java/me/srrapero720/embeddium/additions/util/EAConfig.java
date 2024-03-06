package me.srrapero720.embeddium.additions.util;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.nio.charset.StandardCharsets;

import static me.srrapero720.embeddium.additions.EmbeddiumAdditions.LOGGER;

public class EAConfig {
    public static final Marker IT = MarkerManager.getMarker("Config");

    private static final ForgeConfigSpec SPECS;
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final EnumValue<FPSDisplayText> fpsTextMode;
    public static final EnumValue<HGravity> fpsTextGravity;
    public static final IntValue fpsTextVerticalMargin;
    public static final IntValue fpsTextHorizontalMargin;
    public static final BooleanValue fpsTextboxShadow;
    public static final ConfigValue<String> fpsText;

    static {
        { // FPSTextDisplay
            BUILDER.comment("Configure FPS Text Display settings").push("FPSTextDisplay");

            fpsTextMode = BUILDER
                    .comment(
                            "Define what is the aspect of the FPS Text Display",
                            "CUSTOM - let you change the design by yourself"
                    ).defineEnum("textMode", FPSDisplayText.DETAILED_NONE);
            fpsTextGravity = BUILDER
                    .comment(
                            "Change where is placed the FPS Text Display"
                    ).defineEnum("textPosition", HGravity.LEFT);
            fpsTextVerticalMargin = BUILDER
                    .comment(
                            "Sets margin distance at the corner in V-Axis"
                    ).defineInRange("textVerticalMargin", 12, 0, 48);
            fpsTextHorizontalMargin = BUILDER
                    .comment(
                            "Sets margin distance at the corner in H-Axis"
                    ).defineInRange("textHorizontalMargin", 12, 0, 64);
            fpsTextboxShadow = BUILDER
                    .comment(
                            "Render a shadow behind the FPS text similar to the F3 overlay"
                    ).define("textboxShadow", false);
            fpsText = BUILDER
                    .comment(
                            "Configure the FPS Text Display using the placeholders",
                            "Numeric vars like {fps} or {os.gpu} are colored by danger and is not cleared for next text, you should reset it using §r",
                            "Supports coloring using § symbol, check https://minecraft.wiki/w/Formatting_codes",
                            "{fps} - FPS Counter",
                            "{fps.avg} - AVG FPS",
                            "{fps.min} - MIN FPS",
                            "{os.name} - Simplified short version of the OS name (WIN, MAC, LUX)",
                            "{os.mem} - RAM usage in percent without % symbol",
                            "{os.gpu} - GPU usage in percent without % symbol",
                            "{text.fps} - 'FPS' translatable text",
                            "{text.min} - 'MIN' translatable text",
                            "{text.avg} - 'AVG' translatable text",
                            "{text.os} - 'OS' translatable text",
                            "{text.mem} - 'MEM' translatable text",
                            "{text.gpu} - 'GPU' translatable text"
                    )
                    .define("text", "{fps} §r| {fps.min} §r{text.min} | {fps.avg} §r{text.avg} - {os.gpu} §r{text.gpu} | §r{os.mem} §r{text.mem}");

            BUILDER.pop();
        }

        SPECS = BUILDER.build();
    }

    public static String getFpsText() {
        return switch (fpsTextMode.get()) {
            case NONE -> null;
            case CUSTOM -> fpsText.get();
            default -> fpsTextMode.get().placeholder;
        };
    }

    public static void init() {
        if (SPECS.isLoaded()) return;
        LOGGER.info(IT, "Loading config");

        final var configPath = FMLPaths.CONFIGDIR.get().resolve("embeddium++additions.toml");
        try { // This helps to early-load config and also handle whenever it crashes by run out of space?
            final var configData = CommentedFileConfig.builder(configPath)
                    .charset(StandardCharsets.UTF_8)
                    .writingMode(WritingMode.REPLACE)
                    .autosave()
                    .autoreload()
                    .build();

            configData.load();
            SPECS.setConfig(configData);
        } catch (Exception e) {
            LOGGER.error(IT, "Failed to load config... attempting to recover", e);
            final var file = configPath.toFile();
            if (!file.exists()) throw new RuntimeException("Failed to read configuration file");
            if (!file.delete()) throw new RuntimeException("Failed to remove corrupted configuration file");
            init();
        }
    }

    public enum FPSDisplayText {
        CUSTOM(null),
        NONE(null),

        BASIC_NONE("{fps} §r{text.fps}§r"),
        DETAILED_NONE("{fps} §r| {text.min} {fps.min} §r| {text.avg} {fps.avg}§r"),
        NONE_BASIC("{text.gpu} {os.gpu}%§r"),
        NONE_DETAILED("{text.gpu} {os.gpu}% §r| {text.mem} {os.mem}%§r"),

        BASIC_BASIC(BASIC_NONE + " - " + NONE_BASIC),
        DETAILED_BASIC(DETAILED_NONE + " - " + NONE_BASIC),
        BASIC_DETAILED(BASIC_NONE + " - " + NONE_DETAILED),
        DETAILED_DETAILED(DETAILED_NONE + " - " + NONE_DETAILED);

        private final String placeholder;
        FPSDisplayText(String placeholder) {
            this.placeholder = placeholder;
        }

        @Override
        public String toString() {
            return placeholder;
        }
    }

    public enum HGravity {
        LEFT, CENTER, RIGHT
    }
}