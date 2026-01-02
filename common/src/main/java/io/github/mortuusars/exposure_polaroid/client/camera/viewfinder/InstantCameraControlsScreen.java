package io.github.mortuusars.exposure_polaroid.client.camera.viewfinder;

import io.github.mortuusars.exposure.ModWidgetSprites;
import io.github.mortuusars.exposure.client.camera.viewfinder.Viewfinder;
import io.github.mortuusars.exposure.client.camera.viewfinder.ViewfinderCameraControlsScreen;
import io.github.mortuusars.exposure.world.camera.Camera;
import io.github.mortuusars.exposure_polaroid.ExposurePolaroid;
import io.github.mortuusars.exposure_polaroid.client.gui.screen.camera.button.ExposureSliderButton;
import io.github.mortuusars.exposure_polaroid.client.gui.screen.camera.button.SlideCounterWidget;
import io.github.mortuusars.exposure_polaroid.client.gui.screen.camera.button.ZoomWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

public class InstantCameraControlsScreen extends ViewfinderCameraControlsScreen {
    public static final ModWidgetSprites ZOOM_SPRITES = ModWidgetSprites.withPrefix(
            ExposurePolaroid.resource("camera_controls/zoom"),
            ExposurePolaroid.resource("camera_controls/zoom_disabled"),
            ExposurePolaroid.resource("camera_controls/zoom_highlighted"),49,18);

    public static final ModWidgetSprites SLIDE_COUNTER_SPRITES = ModWidgetSprites.withPrefix(
            ExposurePolaroid.resource("camera_controls/slide_counter"),
            ExposurePolaroid.resource("camera_controls/slide_counter_disabled"),
            ExposurePolaroid.resource("camera_controls/slide_counter_highlighted"),49,18);

    protected static final int SIDE_BUTTONS_WIDTH = 49;

    public InstantCameraControlsScreen(Camera camera, Viewfinder viewfinder) {
        super(camera, viewfinder);
    }

    @Override
    protected void init() {
        refreshMovementKeys();

        leftPos = (width - 256) / 2;
        topPos = Math.round(viewfinder.overlay().getOpening().y + viewfinder.overlay().getOpening().height - 256);

        boolean hasFlash = camera.map((i, s) -> i.getFlash().isAvailable(s)).orElse(false);

        int widgetsWidth = SIDE_BUTTONS_WIDTH + 1 + BUTTON_WIDTH + 1 + (hasFlash ? BUTTON_WIDTH + 1 : 0) + SIDE_BUTTONS_WIDTH;

        int elementX = leftPos + 128 - (widgetsWidth / 2);
        int elementY = topPos + 238;

        // Order of adding influences TAB key behavior

        Button exposureSliderButton = createExposureSliderButton(elementY);
        addRenderableWidget(exposureSliderButton);

        ZoomWidget zoomWidget = new ZoomWidget(elementX, elementY, SIDE_BUTTONS_WIDTH, BUTTON_HEIGHT, ZOOM_SPRITES, camera);
        zoomWidget.setTooltip(Tooltip.create(Component.translatable("gui.exposure_polaroid.camera_controls.zoom.tooltip")));
        addRenderableOnly(zoomWidget);
        elementX += zoomWidget.getWidth();

        addSeparator(elementX, elementY);
        elementX += SEPARATOR_WIDTH;

        Button selfTimerButton = createSelfTimerButton();
        selfTimerButton.setX(elementX);
        selfTimerButton.setY(elementY);
        addRenderableWidget(selfTimerButton);
        elementX += selfTimerButton.getWidth();

        addSeparator(elementX, elementY);
        elementX += SEPARATOR_WIDTH;

        if (hasFlash) {
            Button flashModeButton = createFlashModeButton();
            flashModeButton.setX(elementX);
            flashModeButton.setY(elementY);
            addRenderableWidget(flashModeButton);
            elementX += flashModeButton.getWidth();

            addSeparator(elementX, elementY);
            elementX += SEPARATOR_WIDTH;
        }

        SlideCounterWidget slideCounterWidget = new SlideCounterWidget(elementX, elementY, SIDE_BUTTONS_WIDTH, BUTTON_HEIGHT, SLIDE_COUNTER_SPRITES, camera);
        addRenderableOnly(slideCounterWidget);
    }

    protected Button createExposureSliderButton(int elementY) {
        ExposureSliderButton exposureSliderButton = new ExposureSliderButton(leftPos + 88, elementY - 15, camera);
        exposureSliderButton.setTooltip(Tooltip.create(Component.translatable("gui.exposure_polaroid.camera_controls.exposure_slider.tooltip")));
        return exposureSliderButton;
    }
}