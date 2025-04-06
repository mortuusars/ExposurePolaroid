package io.github.mortuusars.exposure_polaroid;

import io.github.mortuusars.exposure.Exposure;
import io.github.mortuusars.exposure.client.animation.CameraModelPoses;
import io.github.mortuusars.exposure.client.camera.viewfinder.*;
import io.github.mortuusars.exposure.client.capture.template.CameraCaptureTemplate;
import io.github.mortuusars.exposure.client.capture.template.CaptureTemplates;
import io.github.mortuusars.exposure.client.util.Minecrft;
import io.github.mortuusars.exposure.world.camera.ExposureType;
import io.github.mortuusars.exposure.world.entity.CameraHolder;
import io.github.mortuusars.exposure.world.item.SensitiveFilmItem;
import io.github.mortuusars.exposure.world.item.camera.Attachment;
import io.github.mortuusars.exposure.world.item.camera.CameraItem;
import io.github.mortuusars.exposure_polaroid.client.animation.InstantCameraPoses;
import io.github.mortuusars.exposure_polaroid.client.camera.viewfinder.InstantCameraViewfinder;
import io.github.mortuusars.exposure_polaroid.world.item.InstantCameraItem;
import io.github.mortuusars.exposure_polaroid.world.item.camera.InstantCameraAttachment;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.Entity;

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
                    if (stack.getItem() instanceof CameraItem cameraItem) {
                        Entity holderEntity = entity != null ? entity : stack.getEntityRepresentation();
                        if (holderEntity instanceof CameraHolder holder) {
                            return cameraItem.isOnCooldown(holder, stack) ? 1f - cameraItem.getCooldownPercent(holder, stack) : 0f;
                        }
                    }
                    return 0f;
                });

        ItemProperties.register(ExposurePolaroid.Items.INSTANT_CAMERA.get(), ExposurePolaroid.resource("slide_type"),
                (stack, level, entity, seed) -> {
                    if (InstantCameraAttachment.INSTANT_SLIDE.get(stack).getItem() instanceof SensitiveFilmItem film) {
                        return film.getType() == ExposureType.COLOR ? 1f : 0.5f;
                    }
                    return 0f;
                });
    }

    public static class Models {
        public static final ModelResourceLocation INSTANT_CAMERA_GUI =
                new ModelResourceLocation(ExposurePolaroid.resource("instant_camera_gui"), "standalone");
    }
}
