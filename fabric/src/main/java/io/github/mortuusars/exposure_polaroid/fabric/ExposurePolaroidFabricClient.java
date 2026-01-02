package io.github.mortuusars.exposure_polaroid.fabric;

import io.github.mortuusars.exposure.world.item.camera.CameraItem;
import io.github.mortuusars.exposure_polaroid.ExposurePolaroid;
import io.github.mortuusars.exposure_polaroid.ExposurePolaroidClient;
import io.github.mortuusars.exposure_polaroid.network.fabric.FabricS2CPacketHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

public class ExposurePolaroidFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ExposurePolaroidClient.init();

        ColorProviderRegistry.ITEM.register(CameraItem::getGlassTintColor, ExposurePolaroid.Items.INSTANT_CAMERA.get());

        ModelLoadingPlugin.register(pluginContext ->
                pluginContext.addModels(
                        ExposurePolaroidClient.Models.INSTANT_CAMERA_GUI));

        FabricS2CPacketHandler.register();
    }
}
