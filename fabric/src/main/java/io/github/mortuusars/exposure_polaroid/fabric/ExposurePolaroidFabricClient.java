package io.github.mortuusars.exposure_polaroid.fabric;

import io.github.mortuusars.exposure_polaroid.ExposurePolaroidClient;
import io.github.mortuusars.exposure_polaroid.network.fabric.FabricS2CPacketHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;

public class ExposurePolaroidFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ExposurePolaroidClient.init();

        ModelLoadingPlugin.register(pluginContext ->
                pluginContext.addModels(
                        ExposurePolaroidClient.Models.INSTANT_CAMERA_GUI.id()));

        FabricS2CPacketHandler.register();
    }
}
