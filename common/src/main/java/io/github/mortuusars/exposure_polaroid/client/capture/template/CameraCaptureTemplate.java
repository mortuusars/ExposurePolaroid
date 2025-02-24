package io.github.mortuusars.exposure_polaroid.client.capture.template;

import com.mojang.logging.LogUtils;
import io.github.mortuusars.exposure.Config;
import io.github.mortuusars.exposure.client.capture.Capture;
import io.github.mortuusars.exposure.client.capture.action.CaptureAction;
import io.github.mortuusars.exposure.client.capture.palettizer.Palettizer;
import io.github.mortuusars.exposure.client.capture.template.CaptureTemplate;
import io.github.mortuusars.exposure.client.image.modifier.ImageModifier;
import io.github.mortuusars.exposure.client.capture.saving.ExposureUploader;
import io.github.mortuusars.exposure.client.util.Minecrft;
import io.github.mortuusars.exposure.world.camera.ExposureType;
import io.github.mortuusars.exposure.world.camera.capture.CaptureProperties;
import io.github.mortuusars.exposure.data.ColorPalette;
import io.github.mortuusars.exposure.util.cycles.task.EmptyTask;
import io.github.mortuusars.exposure.world.entity.CameraHolder;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import io.github.mortuusars.exposure.util.cycles.task.Task;
import org.slf4j.Logger;

public class CameraCaptureTemplate implements CaptureTemplate {
    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public Task<?> createTask(CaptureProperties data) {
        if (data.exposureId().isEmpty()) {
            LOGGER.error("Failed to create capture task: exposure id cannot be empty. '{}'", data);
            return new EmptyTask<>();
        }

        int entityId = data.cameraHolderEntityId().orElse(Minecrft.player().getId());
        if (!(Minecrft.level().getEntity(entityId) instanceof CameraHolder cameraHolder)) {
            LOGGER.error("Failed to create capture task: camera holder cannot be obtained. '{}'", data);
            return new EmptyTask<>();
        }

        Entity entity = cameraHolder.asEntity();
        ExposureType filmType = data.filmType();
        int frameSize = data.frameSize().orElse(Config.Server.DEFAULT_FRAME_SIZE.getAsInt());
        Holder<ColorPalette> palette = data.getColorPalette(Minecrft.registryAccess());

        return Capture.of(Capture.screenshot(),
                        CaptureAction.setCameraEntity(entity),
                        CaptureAction.hideGui(),
                        CaptureAction.forceRegularOrSelfieCamera(),
                        CaptureAction.optional(Config.Client.KEEP_POST_EFFECT.isFalse(), CaptureAction::disablePostEffect),
                        CaptureAction.modifyGamma(data.getShutterSpeed()),
                        CaptureAction.optional(data.flash(), () -> CaptureAction.flash(entity)))
                .handleErrorAndGetResult(printCasualErrorInChat())
                .thenAsync(ImageModifier.chain(
                        ImageModifier.Crop.SQUARE_CENTER,
                        ImageModifier.Crop.factor(data.cropFactor()),
                        ImageModifier.Resize.to(frameSize),
                        ImageModifier.brightness(data.getShutterSpeed()),
                        ImageModifier.optional(filmType == ExposureType.BLACK_AND_WHITE,
                                data.singleChannel()
                                        .map(ImageModifier::singleChannelBlackAndWhite)
                                        .orElse(ImageModifier.BLACK_AND_WHITE))))
                .thenAsync(Palettizer.DITHERED.palettizeAndClose(palette.value()))
                .thenAsync(convertToExposureData(palette, createExposureTag(cameraHolder.getPlayerExecutingExposure(), data, false)))
                .acceptAsync(image -> ExposureUploader.upload(data.exposureId(), image))
                .onError(printCasualErrorInChat());
    }
}
