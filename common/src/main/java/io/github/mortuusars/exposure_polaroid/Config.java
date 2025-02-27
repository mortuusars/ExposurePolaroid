package io.github.mortuusars.exposure_polaroid;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Using ForgeConfigApiPort on fabric allows using forge config in both environments and without extra dependencies on forge.
 */
public class Config {
    public static class Server {
        public static final ModConfigSpec SPEC;

//        public static final ModConfigSpec.IntValue DEFAULT_FRAME_SIZE;

        static {
            ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

            SPEC = builder.build();
        }
    }

    public static class Client {
        public static final ModConfigSpec SPEC;

        // UI
        public static final ModConfigSpec.BooleanValue INSTANT_CAMERA_SHOW_TOOLTIP_DETAILS;
        public static final ModConfigSpec.BooleanValue INSTANT_CAMERA_SHOW_SLIDES_COUNT_IN_TOOLTIP;
        public static final ModConfigSpec.BooleanValue INSTANT_CAMERA_SHOW_FULLNESS_BAR_ON_ITEM;

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

            SPEC = builder.build();
        }
    }
}
