package io.github.mortuusars.exposure_polaroid.client.camera.viewfinder;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.mortuusars.exposure.client.camera.viewfinder.Viewfinder;
import io.github.mortuusars.exposure.client.camera.viewfinder.ViewfinderOverlay;
import io.github.mortuusars.exposure.client.util.GuiUtil;
import io.github.mortuusars.exposure.world.camera.Camera;
import io.github.mortuusars.exposure_polaroid.ExposurePolaroid;
import io.github.mortuusars.exposure_polaroid.world.item.InstantCameraItem;
import io.github.mortuusars.exposure_polaroid.world.item.camera.InstantCameraAttachment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class InstantCameraViewfinderOverlay extends ViewfinderOverlay {
    public static final ResourceLocation VIEWFINDER_TEXTURE = ExposurePolaroid.resource("textures/gui/viewfinder/viewfinder.png");
    public static final ResourceLocation NO_SLIDES_ICON_TEXTURE = ExposurePolaroid.resource("textures/gui/viewfinder/no_slides.png");
    public static final ResourceLocation REMAINING_SLIDES_ICON_TEXTURE = ExposurePolaroid.resource("textures/gui/viewfinder/remaining_slides.png");

    public InstantCameraViewfinderOverlay(Camera camera, Viewfinder viewfinder) {
        super(camera, viewfinder);
    }

    @Override
    protected void drawViewfinderTexture(GuiGraphics guiGraphics) {
        GuiUtil.blit(VIEWFINDER_TEXTURE, guiGraphics.pose(), opening, 0, 0, (int) opening.width, (int) opening.height, 0);
    }

    @Override
    protected void renderStatusIcons(PoseStack poseStack, ItemStack cameraStack) {
        ItemStack slideStack = InstantCameraAttachment.INSTANT_SLIDE.get(cameraStack).getForReading();
        if (slideStack.isEmpty()) {
            renderNoSlidesIcon(poseStack);
            return;
        }

        renderRemainingSlidesIcon(poseStack, cameraStack);
    }

    protected void renderNoSlidesIcon(PoseStack poseStack) {
        RenderSystem.setShaderTexture(0, NO_SLIDES_ICON_TEXTURE);
        int x = (int) ((opening.x + (opening.width / 2) - 12));
        int y = (int) (opening.y + opening.height - 18);
        GuiUtil.blit(poseStack, x, y, 23, 18, 0, 0, 23, 18, 0);
    }

    protected void renderRemainingSlidesIcon(PoseStack poseStack, ItemStack cameraStack) {
        if (!(cameraStack.getItem() instanceof InstantCameraItem instantCamera)) return;

        int maxSlides = instantCamera.getMaxSlideCount();
        int remainingSlides = instantCamera.getRemainingSlides(cameraStack);

        if (maxSlides > 5 && remainingSlides <= 3) {
            RenderSystem.setShaderTexture(0, REMAINING_SLIDES_ICON_TEXTURE);
            float x = (int) (opening.x + (opening.width / 2) - 17);
            float y = (int) (opening.y + opening.height - 15);
            int vOffset = (remainingSlides - 1) * 15;
            GuiUtil.blit(poseStack, x, y, 33, 15, 0, vOffset, 33, 45, 0);
        }
    }
}
