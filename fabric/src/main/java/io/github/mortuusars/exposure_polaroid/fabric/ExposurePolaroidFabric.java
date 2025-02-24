package io.github.mortuusars.exposure_polaroid.fabric;

import io.github.mortuusars.exposure.ExposureClient;
import io.github.mortuusars.exposure_polaroid.ExposurePolaroid;
import io.github.mortuusars.exposure_polaroid.ExposurePolaroidClient;
import io.github.mortuusars.exposure_polaroid.network.fabric.FabricC2SPackets;
import io.github.mortuusars.exposure_polaroid.network.fabric.FabricS2CPackets;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;

public class ExposurePolaroidFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ExposurePolaroid.init();

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(content -> {
            content.accept(ExposurePolaroid.Items.INSTANT_CAMERA.get());
            content.accept(ExposurePolaroid.Items.INSTANT_COLOR_SLIDE.get());
            content.accept(ExposurePolaroid.Items.INSTANT_BLACK_AND_WHITE_SLIDE.get());
        });

        ModelLoadingPlugin.register(pluginContext ->
                pluginContext.addModels(
                        ExposurePolaroidClient.Models.INSTANT_CAMERA_GUI.id()));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
        });

        ExposurePolaroid.Stats.register();

        FabricC2SPackets.register();
        FabricS2CPackets.register();
    }
}
