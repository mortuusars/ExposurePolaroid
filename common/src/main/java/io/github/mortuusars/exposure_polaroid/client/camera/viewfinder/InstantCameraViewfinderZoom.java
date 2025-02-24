package io.github.mortuusars.exposure_polaroid.client.camera.viewfinder;

import io.github.mortuusars.exposure.client.animation.Animation;
import io.github.mortuusars.exposure.client.animation.EasingFunction;
import io.github.mortuusars.exposure.client.camera.viewfinder.Viewfinder;
import io.github.mortuusars.exposure.client.camera.viewfinder.ViewfinderZoom;
import io.github.mortuusars.exposure.client.util.ZoomDirection;
import io.github.mortuusars.exposure.world.camera.Camera;
import io.github.mortuusars.exposure.world.item.camera.CameraSettings;
import net.minecraft.util.Mth;

public class InstantCameraViewfinderZoom extends ViewfinderZoom {
    public InstantCameraViewfinderZoom(Camera camera, Viewfinder viewfinder) {
        super(camera, viewfinder);
        animation = new Animation(100, EasingFunction.EASE_OUT_EXPO);
    }

    @Override
    public void zoom(ZoomDirection direction, boolean precise) {
        currentFov = getCurrentFov();

        float zoom = CameraSettings.ZOOM.getOrDefault(camera);
        float targetZoom = direction == ZoomDirection.IN ? 1f : 0f;

        if (!Mth.equal(zoom, targetZoom)) {
            targetFov = focalRange.fovFromZoom(targetZoom);
            animation.resetProgress();

            CameraSettings.ZOOM.setAndSync(camera, targetZoom);
        }
    }
}
