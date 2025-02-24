package io.github.mortuusars.exposure_polaroid.world.item.camera;

import io.github.mortuusars.exposure.Exposure;
import io.github.mortuusars.exposure.world.item.camera.Attachment;
import io.github.mortuusars.exposure.world.sound.SoundEffect;
import io.github.mortuusars.exposure_polaroid.ExposurePolaroid;
import io.github.mortuusars.exposure_polaroid.world.item.InstantSlideItem;

import java.util.Optional;

public class InstantCameraAttachment {
    public static final Attachment<InstantSlideItem> INSTANT_SLIDE = new Attachment<>(ExposurePolaroid.resource("instant_slide"),
            Exposure.DataComponents.FILM,
            stack -> stack.getItem() instanceof InstantSlideItem,
            InstantSlideItem.class,
            Optional.of(new SoundEffect(ExposurePolaroid.SoundEvents.INSTANT_CAMERA_SLIDE_INSERT, 0.9F, 1F, 0.15F)),
            Optional.empty());
}
