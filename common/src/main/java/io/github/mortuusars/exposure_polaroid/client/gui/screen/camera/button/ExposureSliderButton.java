package io.github.mortuusars.exposure_polaroid.client.gui.screen.camera.button;

import io.github.mortuusars.exposure.client.camera.CameraClient;
import io.github.mortuusars.exposure.world.camera.Camera;
import io.github.mortuusars.exposure.world.camera.component.ShutterSpeed;
import io.github.mortuusars.exposure.world.item.camera.CameraSettings;
import io.github.mortuusars.exposure_polaroid.ExposurePolaroid;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.List;

public class ExposureSliderButton extends Button {
    public static final ResourceLocation BASE = ExposurePolaroid.resource("camera_controls/exposure_slider_base");

    public static final WidgetSprites SLIDER = new WidgetSprites(
            ExposurePolaroid.resource("camera_controls/exposure_slider"),
            ExposurePolaroid.resource("camera_controls/exposure_slider_disabled"),
            ExposurePolaroid.resource("camera_controls/exposure_slider_highlighted"));

    protected static final int SLIDER_WIDTH = 122;
    protected static final int SLIDER_HEIGHT = 15;
    protected static final int SLIDER_MOVEMENT_RANGE = 58;

    protected final Camera camera;

    public ExposureSliderButton(int x, int y, Camera camera) {
        super(x, y, 79, 15, Component.empty(), b -> {
        }, n -> Component.empty());
        this.camera = camera;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blitSprite(BASE, getX(), getY() + 1, width, height - 1);

        List<ShutterSpeed> shutterSpeeds = camera.map((i, s) -> i.getAvailableShutterSpeeds()).orElse(List.of(ShutterSpeed.DEFAULT));
        if (shutterSpeeds.isEmpty()) {
            shutterSpeeds = List.of(ShutterSpeed.DEFAULT);
        }
        ShutterSpeed shutterSpeed = CameraSettings.SHUTTER_SPEED.getOrDefault(camera);

        int indexOf = shutterSpeeds.indexOf(shutterSpeed);
        int index = indexOf == -1 ? shutterSpeeds.size() / 2 : indexOf;
        float position = (float) (index + 1) / shutterSpeeds.size();

        int offset = Math.round(SLIDER_MOVEMENT_RANGE * position);

        ResourceLocation sliderSprite = SLIDER.get(this.isActive(), this.isHoveredOrFocused());
        guiGraphics.blitSprite(sliderSprite, SLIDER_WIDTH, SLIDER_HEIGHT, offset, 0,
                getX() + 7, getY() + 7, 63, 8);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (scrollY != 0) {
            List<ShutterSpeed> shutterSpeeds = camera.map((i, s) -> i.getAvailableShutterSpeeds()).orElse(List.of(ShutterSpeed.DEFAULT));
            ShutterSpeed shutterSpeed = CameraSettings.SHUTTER_SPEED.getOrDefault(camera);

            //noinspection SuspiciousNameCombination
            int direction = Mth.sign(scrollY);
            int indexOf = shutterSpeeds.indexOf(shutterSpeed);
            int index = indexOf == -1 ? shutterSpeeds.size() / 2 : indexOf;
            index = Mth.clamp(index + direction, 0, shutterSpeeds.size() - 1);

            CameraSettings.SHUTTER_SPEED.setAndSync(camera, shutterSpeeds.get(index));
            return true;
        }
        return false;
    }
}
