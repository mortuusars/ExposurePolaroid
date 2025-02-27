package io.github.mortuusars.exposure_polaroid;

import io.github.mortuusars.exposure.Exposure;
import io.github.mortuusars.exposure.client.animation.CameraModelPoses;
import io.github.mortuusars.exposure.client.camera.viewfinder.*;
import io.github.mortuusars.exposure.client.capture.template.CameraCaptureTemplate;
import io.github.mortuusars.exposure.client.capture.template.CaptureTemplates;
import io.github.mortuusars.exposure.client.util.Minecrft;
import io.github.mortuusars.exposure.world.item.camera.CameraItem;
import io.github.mortuusars.exposure_polaroid.client.animation.InstantCameraPoses;
import io.github.mortuusars.exposure_polaroid.client.camera.viewfinder.InstantCameraViewfinder;
import io.github.mortuusars.exposure_polaroid.world.item.InstantCameraItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.player.Player;

public class ExposurePolaroidClient {
    public static void init() {
        CameraModelPoses.register(ExposurePolaroid.Items.INSTANT_CAMERA.get(), new InstantCameraPoses());

        ViewfinderRegistry.register(ExposurePolaroid.Items.INSTANT_CAMERA.get(), InstantCameraViewfinder::new);

        CaptureTemplates.register(InstantCameraItem.CAPTURE_TYPE, new CameraCaptureTemplate());

        ItemProperties.register(ExposurePolaroid.Items.INSTANT_CAMERA.get(), Exposure.resource("camera_state"),
                (stack, level, entity, seed) -> {
            if (!(stack.getItem() instanceof CameraItem cameraItem) || !cameraItem.isActive(stack)) {
                return 0f;
            }

            if (cameraItem.isInSelfieMode(stack)) {
                // Longer selfie stick for current player (to not obscure the view) and regular for everyone else
                return entity == Minecrft.player() ? 0.2f : 0.3f;
            }

            return 0.1f;
        });

        ItemProperties.register(ExposurePolaroid.Items.INSTANT_CAMERA.get(), ExposurePolaroid.resource("printing"),
                (stack, level, entity, seed) -> {
            if (entity instanceof Player player && player.getCooldowns().isOnCooldown(stack.getItem())) {
                return 1f - player.getCooldowns().getCooldownPercent(stack.getItem(), 0);
            }
            return 0f;
        });
    }

    public static class Models {
        public static final ModelResourceLocation INSTANT_CAMERA_GUI =
                new ModelResourceLocation(ExposurePolaroid.resource("instant_camera_gui"), "standalone");
    }
}
