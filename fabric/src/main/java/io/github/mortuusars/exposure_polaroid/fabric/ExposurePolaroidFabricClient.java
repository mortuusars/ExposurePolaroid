package io.github.mortuusars.exposure_polaroid.fabric;

import io.github.mortuusars.exposure_polaroid.ExposurePolaroidClient;
import io.github.mortuusars.exposure_polaroid.network.fabric.FabricS2CPacketHandler;
import net.fabricmc.api.ClientModInitializer;

public class ExposurePolaroidFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ExposurePolaroidClient.init();
        FabricS2CPacketHandler.register();
    }
}
