package io.github.mortuusars.exposure_polaroid.client.camera.viewfinder;

import io.github.mortuusars.exposure.client.camera.viewfinder.Viewfinder;
import io.github.mortuusars.exposure.client.camera.viewfinder.ViewfinderCameraControlsScreen;
import io.github.mortuusars.exposure.client.gui.screen.camera.button.FrameCounterButton;
import io.github.mortuusars.exposure.world.camera.Camera;
import io.github.mortuusars.exposure.world.item.camera.CameraItem;
import io.github.mortuusars.exposure_polaroid.ExposurePolaroid;
import io.github.mortuusars.exposure_polaroid.client.gui.screen.camera.button.ExposureSliderButton;
import io.github.mortuusars.exposure_polaroid.client.gui.screen.camera.button.SlideCounterWidget;
import io.github.mortuusars.exposure_polaroid.client.gui.screen.camera.button.ZoomWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class InstantCameraControlsScreen extends ViewfinderCameraControlsScreen {
    public static final WidgetSprites ZOOM_SPRITES = new WidgetSprites(
            ExposurePolaroid.resource("camera_controls/zoom"),
            ExposurePolaroid.resource("camera_controls/zoom_disabled"),
            ExposurePolaroid.resource("camera_controls/zoom_highlighted"));

    public static final WidgetSprites SLIDE_COUNTER_SPRITES = new WidgetSprites(
            ExposurePolaroid.resource("camera_controls/slide_counter"),
            ExposurePolaroid.resource("camera_controls/slide_counter_disabled"),
            ExposurePolaroid.resource("camera_controls/slide_counter_highlighted"));

    public static final ResourceLocation SEPARATOR_SPRITE = ExposurePolaroid.resource("camera_controls/button_separator");

    protected static final int SIDE_BUTTONS_WIDTH = 45;

    public InstantCameraControlsScreen(Camera camera, Viewfinder viewfinder) {
        super(camera, viewfinder);
    }

    @Override
    protected void init() {
        refreshMovementKeys();

        leftPos = (width - 256) / 2;
        topPos = Math.round(viewfinder.overlay().getOpening().y + viewfinder.overlay().getOpening().height - 256);

        boolean hasFlash = camera.map(CameraItem::hasFlash).orElse(false);

        int widgetsWidth = SIDE_BUTTONS_WIDTH + 1 + (hasFlash ? BUTTON_WIDTH + 1 : 0) + SIDE_BUTTONS_WIDTH;

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