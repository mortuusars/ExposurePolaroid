package io.github.mortuusars.exposure_polaroid;

import com.google.common.base.Preconditions;
import com.mojang.logging.LogUtils;
import io.github.mortuusars.exposure.Exposure;
import io.github.mortuusars.exposure.world.camera.ExposureType;
import io.github.mortuusars.exposure.world.item.camera.Shutter;
import io.github.mortuusars.exposure_polaroid.world.item.InstantCameraItem;
import io.github.mortuusars.exposure_polaroid.world.item.InstantSlideItem;
import io.github.mortuusars.exposure_polaroid.world.item.camera.InstantCameraShutter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatFormatter;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;


public class ExposurePolaroid {
    public static final String ID = "exposure_polaroid";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        Blocks.init();
        BlockEntityTypes.init();
        EntityTypes.init();
        Items.init();
        DataComponents.init();
        CriteriaTriggers.init();
        ItemSubPredicates.init();
        MenuTypes.init();
        RecipeSerializers.init();
        SoundEvents.init();
        ArgumentTypes.init();
    }

    /**
     * Creates resource location in the mod namespace with the given filePath.
     */
    public static ResourceLocation resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }

    public static class Blocks {
        static void init() {
        }
    }

    public static class BlockEntityTypes {
        static void init() {
        }
    }

    public static class Items {
        public static final Supplier<InstantCameraItem> INSTANT_CAMERA = Register.item("instant_camera",
                () -> new InstantCameraItem(new InstantCameraShutter(), new Item.Properties()
                        .stacksTo(1)
                        .component(Exposure.DataComponents.CAMERA_ACTIVE, false)));

        public static final Supplier<InstantSlideItem> INSTANT_COLOR_SLIDE = Register.item("instant_color_slide",
                () -> new InstantSlideItem(ExposureType.COLOR, new Item.Properties()));
        public static final Supplier<InstantSlideItem> INSTANT_BLACK_AND_WHITE_SLIDE = Register.item("instant_black_and_white_slide",
                () -> new InstantSlideItem(ExposureType.BLACK_AND_WHITE, new Item.Properties()));

        static void init() {
        }
    }

    public static class DataComponents {
        static void init() {
        }
    }

    public static class EntityTypes {
        static void init() {
        }
    }

    public static class MenuTypes {
        static void init() {
        }
    }

    public static class RecipeSerializers {
        static void init() {
        }
    }

    public static class SoundEvents {
        public static final Supplier<SoundEvent> INSTANT_CAMERA_VIEWFINDER_OPEN = register("item", "instant_camera.viewfinder_open");
        public static final Supplier<SoundEvent> INSTANT_CAMERA_VIEWFINDER_CLOSE = register("item", "instant_camera.viewfinder_close");
        public static final Supplier<SoundEvent> INSTANT_CAMERA_RELEASE = register("item", "instant_camera.release");
        public static final Supplier<SoundEvent> INSTANT_CAMERA_SLIDE_INSERT = register("item", "instant_camera.slide_insert");

        private static Supplier<SoundEvent> register(String category, String key) {
            Preconditions.checkState(category != null && !category.isEmpty(), "'category' should not be empty.");
            Preconditions.checkState(key != null && !key.isEmpty(), "'key' should not be empty.");
            String path = category + "." + key;
            return Register.soundEvent(path, () -> SoundEvent.createVariableRangeEvent(ExposurePolaroid.resource(path)));
        }

        static void init() {
        }
    }

    public static class Stats {
        public static final Map<ResourceLocation, StatFormatter> STATS = new HashMap<>();

        @SuppressWarnings("SameParameterValue")
        private static ResourceLocation register(ResourceLocation location, StatFormatter formatter) {
            STATS.put(location, formatter);
            return location;
        }

        public static void register() {
            STATS.forEach((location, formatter) -> {
                net.minecraft.core.Registry.register(BuiltInRegistries.CUSTOM_STAT, location, location);
                net.minecraft.stats.Stats.CUSTOM.get(location, formatter);
            });
        }
    }

    public static class CriteriaTriggers {
        public static void init() {
        }
    }

    public static class ItemSubPredicates {
        public static void init() {
        }
    }

    public static class LootTables {
    }

    public static class Tags {
        public static class Items {
        }

        public static class Blocks {
        }
    }

    public static class ArgumentTypes {
        public static void init() {
        }
    }

    public static class Registries {
    }
}
