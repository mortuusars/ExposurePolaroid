package io.github.mortuusars.exposure_polaroid.client.gui.screen.camera.button;

import com.mojang.blaze3d.platform.InputConstants;
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
        float position = 1f - ((float) (index) / (shutterSpeeds.size() - 1));

        int offset = Math.round(SLIDER_MOVEMENT_RANGE * position);

        ResourceLocation sliderSprite = SLIDER.get(this.isActive(), this.isHoveredOrFocused());
        guiGraphics.blitSprite(sliderSprite, SLIDER_WIDTH, SLIDER_HEIGHT, offset, 0,
                getX() + 8, getY(), 64, 15);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (scrollY != 0) {
            //noinspection SuspiciousNameCombination
            moveSlider(Mth.sign(scrollY));
            return true;
        }
        return false;
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        setSliderToMousePosition(mouseX);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (clicked(mouseX, mouseY)) {
            if (isValidClickButton(button)) {
                setSliderToMousePosition(mouseX);
                return true;
            }

            if (button == InputConstants.MOUSE_BUTTON_RIGHT) {
                List<ShutterSpeed> shutterSpeeds = camera.map((i, s) -> i.getAvailableShutterSpeeds()).orElse(List.of(ShutterSpeed.DEFAULT));
                if (shutterSpeeds.isEmpty()) return true;

                int defaultIndex = shutterSpeeds.indexOf(ShutterSpeed.DEFAULT);
                int index = defaultIndex != -1 ? defaultIndex : shutterSpeeds.size() / 2;

                ShutterSpeed shutterSpeed = CameraSettings.SHUTTER_SPEED.getOrDefault(camera);
                ShutterSpeed newShutterSpeed = shutterSpeeds.get(index);

                if (!shutterSpeed.equals(newShutterSpeed)) {
                    CameraSettings.SHUTTER_SPEED.setAndSync(camera, newShutterSpeed);
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == InputConstants.KEY_LEFT) {
            moveSlider(-1);
            return true;
        } else if (keyCode == InputConstants.KEY_RIGHT) {
            moveSlider(1);
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    protected void setSliderToMousePosition(double mouseX) {
        List<ShutterSpeed> shutterSpeeds = camera.map((i, s) -> i.getAvailableShutterSpeeds()).orElse(List.of(ShutterSpeed.DEFAULT));
        if (shutterSpeeds.isEmpty()) return;

        int start = getX() + 8;
        int end = getX() + 8 + 64;
        int range = end - start;
        int distancePerNotch = range / shutterSpeeds.size();

        int index = (int) Mth.clamp((mouseX - start) / distancePerNotch, 0, shutterSpeeds.size() - 1);

        ShutterSpeed shutterSpeed = CameraSettings.SHUTTER_SPEED.getOrDefault(camera);
        ShutterSpeed newShutterSpeed = shutterSpeeds.get(index);

        if (!shutterSpeed.equals(newShutterSpeed)) {
            CameraSettings.SHUTTER_SPEED.setAndSync(camera, newShutterSpeed);
        }
    }

    protected boolean moveSlider(int direction) {
        direction = Mth.sign(direction);

        List<ShutterSpeed> shutterSpeeds = camera.map((i, s) -> i.getAvailableShutterSpeeds()).orElse(List.of(ShutterSpeed.DEFAULT));
        ShutterSpeed currentShutterSpeed = CameraSettings.SHUTTER_SPEED.getOrDefault(camera);

        int indexOf = shutterSpeeds.indexOf(currentShutterSpeed);
        int index = indexOf == -1 ? shutterSpeeds.size() / 2 : indexOf;
        index = Mth.clamp(index + direction, 0, shutterSpeeds.size() - 1);

        ShutterSpeed newShutterSpeed = shutterSpeeds.get(index);

        if (!currentShutterSpeed.equals(newShutterSpeed)) {
            CameraSettings.SHUTTER_SPEED.setAndSync(camera, newShutterSpeed);
            return true;
        }

        return false;
    }
}
