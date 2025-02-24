package io.github.mortuusars.exposure_polaroid.client.camera.viewfinder;

import io.github.mortuusars.exposure.client.camera.viewfinder.*;
import io.github.mortuusars.exposure.world.camera.Camera;

public class InstantCameraViewfinder extends Viewfinder {
    public InstantCameraViewfinder(Camera camera,
                                   ComponentConstructor<ViewfinderZoom> zoom,
                                   ComponentConstructor<ViewfinderOverlay> overlay,
                                   ComponentConstructor<ViewfinderShader> shader,
                                   ComponentConstructor<ViewfinderCameraControlsScreen> controlsScreen) {
        super(camera, zoom, overlay, shader, controlsScreen);
    }

    @Override
    public float getCameraYOffset() {
        return 0;
    }
}
