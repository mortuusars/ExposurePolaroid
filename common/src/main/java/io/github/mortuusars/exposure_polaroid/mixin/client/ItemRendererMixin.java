package io.github.mortuusars.exposure_polaroid.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.mortuusars.exposure.PlatformHelperClient;
import io.github.mortuusars.exposure.client.util.Minecrft;
import io.github.mortuusars.exposure_polaroid.ExposurePolaroid;
import io.github.mortuusars.exposure_polaroid.ExposurePolaroidClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @ModifyVariable(method = "render", at = @At("HEAD"), argsOnly = true)
    BakedModel renderItem(BakedModel model, ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand,
                          PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (Minecraft.getInstance().level != null && itemStack.is(ExposurePolaroid.Items.INSTANT_CAMERA.get()) && displayContext == ItemDisplayContext.GUI) {
            BakedModel guiModel = PlatformHelperClient.getModel(ExposurePolaroidClient.Models.INSTANT_CAMERA_GUI);
            return guiModel.getOverrides().resolve(guiModel, itemStack, Minecrft.level(), Minecrft.player(), 0);
        }
        return model;
    }
}