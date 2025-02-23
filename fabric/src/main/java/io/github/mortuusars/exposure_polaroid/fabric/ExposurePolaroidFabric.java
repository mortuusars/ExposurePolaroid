package io.github.mortuusars.exposure_polaroid.fabric;

import io.github.mortuusars.exposure_polaroid.ExposurePolaroid;
import io.github.mortuusars.exposure_polaroid.network.fabric.FabricC2SPackets;
import io.github.mortuusars.exposure_polaroid.network.fabric.FabricS2CPackets;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ExposurePolaroidFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ExposurePolaroid.init();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
        });

        ExposurePolaroid.Stats.register();

        FabricC2SPackets.register();
        FabricS2CPackets.register();
    }
}
