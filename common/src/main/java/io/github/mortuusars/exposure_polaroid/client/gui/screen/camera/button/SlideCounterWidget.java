package io.github.mortuusars.exposure_polaroid.client.gui.screen.camera.button;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.mortuusars.exposure.Config;
import io.github.mortuusars.exposure.client.util.Minecrft;
import io.github.mortuusars.exposure.world.camera.Camera;
import io.github.mortuusars.exposure.world.item.FilmRollItem;
import io.github.mortuusars.exposure.world.item.camera.Attachment;
import io.github.mortuusars.exposure.world.item.camera.CameraSettings;
import io.github.mortuusars.exposure_polaroid.world.item.InstantCameraItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public class SlideCounterWidget extends AbstractWidget {
    protected final WidgetSprites sprites;
    protected final Camera camera;
    protected final int secondaryFontColor;
    protected final int mainFontColor;

    public SlideCounterWidget(int x, int y, int width, int height, WidgetSprites sprites, Camera camera) {
        super(x, y, width, height, Component.empty());
        this.sprites = sprites;
        this.camera = camera;
        mainFontColor = Config.getColor(Config.Client.VIEWFINDER_FONT_MAIN_COLOR);
        secondaryFontColor = Config.getColor(Config.Client.VIEWFINDER_FONT_SECONDARY_COLOR);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        MutableComponent tooltipComponent = Component.translatable("gui.exposure_polaroid.camera_controls.slide_counter.tooltip");
        if (!cameraHasSlides()) {
            tooltipComponent.append(CommonComponents.NEW_LINE)
                    .append(Component.translatable("gui.exposure_polaroid.camera_controls.slide_counter.tooltip.no_slides")
                            .withStyle(Style.EMPTY.withColor(0xdd6357)));
        }
        setTooltip(Tooltip.create(tooltipComponent));

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        ResourceLocation sliderSprite = sprites.get(this.isActive(), this.isHoveredOrFocused());
        guiGraphics.blitSprite(sliderSprite, getX(), getY(), width, height);

        String text = createText();
        Font font = Minecraft.getInstance().font;
        int textWidth = font.width(text);
        int xPos = 15 + (27 - textWidth) / 2;

        guiGraphics.drawString(font, text, getX() + xPos, getY() + 8, secondaryFontColor, false);
        guiGraphics.drawString(font, text, getX() + xPos, getY() + 7, mainFontColor, false);
    }

    protected String createText() {
        return camera
                .map((i, s) -> {
                    if (!(i instanceof InstantCameraItem instantCamera) || instantCamera.getRemainingSlides(s) == 0) {
                        return "-";
                    }
                    int remainingSlides = instantCamera.getRemainingSlides(s);
                    int maxSlides = instantCamera.getMaxSlideCount();
                    return remainingSlides + "/" + maxSlides;
                })
                .orElse("-");
    }


    protected boolean cameraHasSlides() {
        return camera
                .map((i, s) -> i instanceof InstantCameraItem instantCamera && instantCamera.getRemainingSlides(s) > 0)
                .orElse(false);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }
}
