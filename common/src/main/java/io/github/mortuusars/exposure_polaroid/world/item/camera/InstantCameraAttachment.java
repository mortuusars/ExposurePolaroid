package io.github.mortuusars.exposure_polaroid.world.item.camera;

import io.github.mortuusars.exposure.Exposure;
import io.github.mortuusars.exposure.world.item.camera.Attachment;
import io.github.mortuusars.exposure.world.sound.SoundEffect;
import io.github.mortuusars.exposure_polaroid.Config;
import io.github.mortuusars.exposure_polaroid.ExposurePolaroid;
import io.github.mortuusars.exposure_polaroid.world.item.InstantSlideItem;

import java.util.Optional;

public class InstantCameraAttachment {
    public static final Attachment<InstantSlideItem> INSTANT_SLIDE = new Attachment<>(ExposurePolaroid.resource("instant_slide"),
            "film",
            stack -> stack.getItem() instanceof InstantSlideItem,
            InstantSlideItem.class,
            Config.Server.INSTANT_CAMERA_SLIDE_CAPACITY,
            Optional.of(new SoundEffect(ExposurePolaroid.SoundEvents.INSTANT_CAMERA_SLIDE_INSERT, 1F, 1F, 0.15F)),
            Optional.of(new SoundEffect(ExposurePolaroid.SoundEvents.INSTANT_CAMERA_SLIDE_REMOVE, 1F, 1F, 0.15F)));
}
