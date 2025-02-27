package io.github.mortuusars.exposure_polaroid.world.item.camera;

import io.github.mortuusars.exposure.world.entity.CameraHolder;
import io.github.mortuusars.exposure.world.item.camera.Shutter;
import io.github.mortuusars.exposure.world.sound.Sound;
import io.github.mortuusars.exposure_polaroid.ExposurePolaroid;
import net.minecraft.world.entity.Entity;

public class InstantCameraShutter extends Shutter {
    @Override
    public void playOpenSound(CameraHolder holder) {
        Entity entity = holder.asEntity();
        Sound.play(entity, ExposurePolaroid.SoundEvents.INSTANT_CAMERA_RELEASE.get(), entity.getSoundSource(), 1.2f, 1.0f, 0.2f);
    }

    @Override
    public void playCloseSound(CameraHolder holder) {
    }
}
