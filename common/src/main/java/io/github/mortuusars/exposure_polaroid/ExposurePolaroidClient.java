package io.github.mortuusars.exposure_polaroid;

import io.github.mortuusars.exposure.client.camera.viewfinder.*;
import io.github.mortuusars.exposure.client.capture.template.CameraCaptureTemplate;
import io.github.mortuusars.exposure.client.capture.template.CaptureTemplates;
import io.github.mortuusars.exposure_polaroid.client.camera.viewfinder.InstantCameraControlsScreen;
import io.github.mortuusars.exposure_polaroid.client.camera.viewfinder.InstantCameraViewfinder;
import io.github.mortuusars.exposure_polaroid.client.camera.viewfinder.InstantCameraViewfinderOverlay;
import io.github.mortuusars.exposure_polaroid.client.camera.viewfinder.InstantCameraViewfinderZoom;
import io.github.mortuusars.exposure_polaroid.world.item.InstantCameraItem;
import net.minecraft.client.resources.model.ModelResourceLocation;

public class ExposurePolaroidClient {
    public static void init() {
        ViewfinderRegistry.register(ExposurePolaroid.Items.INSTANT_CAMERA.get(), camera ->
                new InstantCameraViewfinder(camera,
                        InstantCameraViewfinderZoom::new,
                        InstantCameraViewfinderOverlay::new,
                        ViewfinderShader::new,
                        InstantCameraControlsScreen::new));

        CaptureTemplates.register(InstantCameraItem.CAPTURE_TYPE, new CameraCaptureTemplate());
    }

    public static class Models {
        public static final ModelResourceLocation INSTANT_CAMERA_GUI =
                new ModelResourceLocation(ExposurePolaroid.resource("instant_camera_gui"), "standalone");
    }
}
