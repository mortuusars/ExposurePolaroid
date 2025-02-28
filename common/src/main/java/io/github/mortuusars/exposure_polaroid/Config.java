package io.github.mortuusars.exposure_polaroid;

import io.github.mortuusars.exposure.Exposure;
import io.github.mortuusars.exposure.util.color.Color;
import io.github.mortuusars.exposure.world.camera.component.FocalRange;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Using ForgeConfigApiPort on fabric allows using forge config in both environments and without extra dependencies on forge.
 */
public class Config {
    public static class Server {
        public static final ModConfigSpec SPEC;

        public static final ModConfigSpec.ConfigValue<String> INSTANT_CAMERA_DEFAULT_FOCAL_RANGE;
        public static final ModConfigSpec.IntValue DEFAULT_FRAME_SIZE;
        public static final ModConfigSpec.IntValue SLIDE_CAPACITY;

        static {
            ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

            {
                builder.push("instant_camera");
                INSTANT_CAMERA_DEFAULT_FOCAL_RANGE = builder
                        .comment("Min/max focal ranges of an Instant Camera.",
                                "Allowed range: " + FocalRange.ALLOWED_MIN + "-" + FocalRange.ALLOWED_MAX,
                                "Default: 20-40")
                        .define("default_focal_range", "20-40");
                DEFAULT_FRAME_SIZE = builder
                        .comment("Default size of an exposure image taken with Instant Camera. High values take more disk space and can cause lag. Default: 240")
                        .defineInRange("default_frame_size", 240, 1, 2048);
                SLIDE_CAPACITY = builder
                        .comment("How many Slides can Instant Camera hold at once. Default: 12")
                        .defineInRange("slide_capacity", 12, 1, 64);
                builder.pop();
            }

            SPEC = builder.build();
        }
    }

    public static class Client {
        public static final ModConfigSpec SPEC;

        // UI
        public static final ModConfigSpec.BooleanValue INSTANT_CAMERA_SHOW_TOOLTIP_DETAILS;
        public static final ModConfigSpec.BooleanValue INSTANT_CAMERA_SHOW_SLIDES_COUNT_IN_TOOLTIP;
        public static final ModConfigSpec.BooleanValue INSTANT_CAMERA_SHOW_FULLNESS_BAR_ON_ITEM;

        // VIEWFINDER
        public static final ModConfigSpec.ConfigValue<String> VIEWFINDER_BACKGROUND_COLOR;
        public static final ModConfigSpec.ConfigValue<String> VIEWFINDER_FONT_MAIN_COLOR;
        public static final ModConfigSpec.ConfigValue<String> VIEWFINDER_FONT_SECONDARY_COLOR;

        static {
            ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

            {
                builder.push("ui");
                INSTANT_CAMERA_SHOW_TOOLTIP_DETAILS = builder
                        .comment("Details about Camera configuring will be shown in Instant Camera item tooltip.")
                        .define("instant_camera_details_tooltip", true);

                INSTANT_CAMERA_SHOW_SLIDES_COUNT_IN_TOOLTIP = builder
                        .comment("Slides count will be shown in the Instant Camera item tooltip.",
                                "Default: true")
                        .define("instant_camera_slides_tooltip", true);

                INSTANT_CAMERA_SHOW_FULLNESS_BAR_ON_ITEM = builder
                        .comment("Slide fullness bar will be shown on the Instant Camera item.",
                                "Default: false")
                        .define("instant_camera_shows_slides_bar", false);
                builder.pop();
            }

            {
                builder.push("viewfinder");
                VIEWFINDER_BACKGROUND_COLOR = builder
                        .comment("Color in hex format. AARRGGBB.").define("background_color", "FA1F1D1B");
                VIEWFINDER_FONT_MAIN_COLOR = builder
                        .comment("Color in hex format. AARRGGBB.").define("font_main_color", "FF2B2622");
                VIEWFINDER_FONT_SECONDARY_COLOR = builder
                        .comment("Color in hex format. AARRGGBB.").define("font_secondary_color", "FF7A736C");
                builder.pop();
            }

            SPEC = builder.build();
        }
    }

    public static int getColor(ModConfigSpec.ConfigValue<String> config) {
        String value = config.get();
        try {
            return Color.fromHex(value).getARGB();
        } catch (Exception e) {
            Exposure.LOGGER.error("{} is not valid ARGB color. {}", value, String.join("/", config.getPath()));
            return 0;
        }
    }
}
