package io.github.mortuusars.exposure_polaroid.client.gui.screen.camera.button;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.mortuusars.exposure.world.camera.Camera;
import io.github.mortuusars.exposure.world.item.camera.CameraSettings;
import io.github.mortuusars.exposure_polaroid.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ZoomWidget extends AbstractWidget {
    protected final WidgetSprites sprites;
    protected final Camera camera;
    protected final int secondaryFontColor;
    protected final int mainFontColor;

    public ZoomWidget(int x, int y, int width, int height, WidgetSprites sprites, Camera camera) {
        super(x, y, width, height, Component.empty());
        this.sprites = sprites;
        this.camera = camera;
        mainFontColor = Config.getColor(Config.Client.VIEWFINDER_FONT_MAIN_COLOR);
        secondaryFontColor = Config.getColor(Config.Client.VIEWFINDER_FONT_SECONDARY_COLOR);
    }

    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        boolean isFar = camera.map((i, s) -> CameraSettings.ZOOM.getOrDefault(s) > 0.5).orElse(false);

        Font font = Minecraft.getInstance().font;
        MutableComponent text = Component.translatable("gui.exposure_polaroid.camera_controls.zoom." + (isFar ? "far" : "near"));
        int textWidth = font.width(text);
        int xPos = 17 + (26 - textWidth) / 2;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        ResourceLocation sliderSprite = sprites.get(this.isActive(), this.isHoveredOrFocused());
        guiGraphics.blitSprite(sliderSprite, getX(), getY(), width, height);

        guiGraphics.drawString(font, text, getX() + xPos, getY() + 8, secondaryFontColor, false);
        guiGraphics.drawString(font, text, getX() + xPos, getY() + 7, mainFontColor, false);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) { }
}