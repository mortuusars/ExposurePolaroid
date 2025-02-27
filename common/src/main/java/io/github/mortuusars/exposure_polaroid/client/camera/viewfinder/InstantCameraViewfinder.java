package io.github.mortuusars.exposure_polaroid.client.camera.viewfinder;

import io.github.mortuusars.exposure.client.camera.viewfinder.*;
import io.github.mortuusars.exposure.world.camera.Camera;

public class InstantCameraViewfinder extends Viewfinder {
    public InstantCameraViewfinder(Camera camera) {
        super(camera);
    }

    @Override
    protected ViewfinderZoom createZoom(Camera camera) {
        return new InstantCameraViewfinderZoom(camera, this);
    }

    @Override
    protected ViewfinderOverlay createOverlay(Camera camera) {
        return new InstantCameraViewfinderOverlay(camera, this);
    }

    @Override
    protected ViewfinderCameraControlsScreen createControlsScreen(Camera camera) {
        return new InstantCameraControlsScreen(camera, this);
    }
}
