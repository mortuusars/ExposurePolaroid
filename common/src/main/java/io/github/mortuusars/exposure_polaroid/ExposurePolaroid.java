package io.github.mortuusars.exposure_polaroid;

import com.google.common.base.Preconditions;
import com.mojang.logging.LogUtils;
import io.github.mortuusars.exposure.world.camera.ExposureType;
import io.github.mortuusars.exposure.world.camera.film.properties.*;
import io.github.mortuusars.exposure_polaroid.world.item.InstantCameraItem;
import io.github.mortuusars.exposure_polaroid.world.item.InstantSlideItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;

import java.util.function.Supplier;


public class ExposurePolaroid {
    public static final String ID = "exposure_polaroid";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        Items.init();
        SoundEvents.init();
    }

    /**
     * Creates resource location in the mod namespace with the given filePath.
     */
    public static ResourceLocation resource(String path) {
        return new ResourceLocation(ID, path);
    }

    public static class Items {
        public static final Supplier<InstantCameraItem> INSTANT_CAMERA = Register.item("instant_camera",
                () -> new InstantCameraItem(new Item.Properties()
                        .stacksTo(1)));

        public static final Supplier<InstantSlideItem> INSTANT_COLOR_SLIDE = Register.item("instant_color_slide",
                () -> new InstantSlideItem(ExposureType.COLOR, new Item.Properties(),
                                FilmStyle.create()
                                        .withContrast(0.2f)
                                        .withLevels(new Levels(0, 135, 255, 25, 255))
                                        .withHSB(new HSB(0f, 0.05f, 0.05f))
                                        .withColorBalance(new ColorBalance(0.03f, 0.01f, -0.01f))));
        public static final Supplier<InstantSlideItem> INSTANT_BLACK_AND_WHITE_SLIDE = Register.item("instant_black_and_white_slide",
                () -> new InstantSlideItem(ExposureType.BLACK_AND_WHITE, new Item.Properties(),
                                FilmStyle.create()
                                        .withContrast(0.2f)
                                        .withLevels(new Levels(0, 135, 255, 25, 255))
                                        .withHSB(new HSB(0f, 0.05f, 0.05f))
                                        .withColorBalance(new ColorBalance(0.03f, 0.01f, -0.01f))));

        public static final Supplier<InstantSlideItem> HIGH_SENSITIVITY_INSTANT_COLOR_SLIDE = Register.item("high_sensitivity_instant_color_slide",
                () -> new InstantSlideItem(ExposureType.COLOR, new Item.Properties(),
                                FilmStyle.create()
                                        .withSensitivity(2f)
                                        .withContrast(0.2f)
                                        .withLevels(new Levels(0, 135, 255, 25, 255))
                                        .withHSB(new HSB(0f, 0.05f, 0.05f))
                                        .withColorBalance(new ColorBalance(0.03f, 0.01f, -0.01f))
                                        .withNoise(0.05f)));
        public static final Supplier<InstantSlideItem> HIGH_SENSITIVITY_INSTANT_BLACK_AND_WHITE_SLIDE = Register.item("high_sensitivity_instant_black_and_white_slide",
                () -> new InstantSlideItem(ExposureType.BLACK_AND_WHITE, new Item.Properties(),
                                FilmStyle.create()
                                        .withSensitivity(2f)
                                        .withContrast(0.2f)
                                        .withLevels(new Levels(0, 135, 255, 25, 255))
                                        .withHSB(new HSB(0f, 0.05f, 0.05f))
                                        .withColorBalance(new ColorBalance(0.03f, 0.01f, -0.01f))
                                        .withNoise(0.05f)));

        static void init() {
        }
    }

    public static class SoundEvents {
        public static final Supplier<SoundEvent> INSTANT_CAMERA_VIEWFINDER_OPEN = register("item", "instant_camera.viewfinder_open");
        public static final Supplier<SoundEvent> INSTANT_CAMERA_VIEWFINDER_CLOSE = register("item", "instant_camera.viewfinder_close");
        public static final Supplier<SoundEvent> INSTANT_CAMERA_RELEASE = register("item", "instant_camera.release");
        public static final Supplier<SoundEvent> INSTANT_CAMERA_SLIDE_INSERT = register("item", "instant_camera.slide_insert");
        public static final Supplier<SoundEvent> INSTANT_CAMERA_SLIDE_REMOVE = register("item", "instant_camera.slide_remove");

        private static Supplier<SoundEvent> register(String category, String key) {
            Preconditions.checkState(category != null && !category.isEmpty(), "'category' should not be empty.");
            Preconditions.checkState(key != null && !key.isEmpty(), "'key' should not be empty.");
            String path = category + "." + key;
            return Register.soundEvent(path, () -> SoundEvent.createVariableRangeEvent(ExposurePolaroid.resource(path)));
        }

        static void init() {
        }
    }

}
