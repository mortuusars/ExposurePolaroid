package io.github.mortuusars.exposure_polaroid.fabric;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import io.github.mortuusars.exposure_polaroid.Config;
import io.github.mortuusars.exposure_polaroid.ExposurePolaroid;
import io.github.mortuusars.exposure_polaroid.ExposurePolaroidClient;
import io.github.mortuusars.exposure_polaroid.network.fabric.FabricC2SPackets;
import io.github.mortuusars.exposure_polaroid.network.fabric.FabricS2CPackets;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.fml.config.ModConfig;

public class ExposurePolaroidFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ExposurePolaroid.init();

        NeoForgeConfigRegistry.INSTANCE.register(ExposurePolaroid.ID, ModConfig.Type.SERVER, Config.Server.SPEC);
        NeoForgeConfigRegistry.INSTANCE.register(ExposurePolaroid.ID, ModConfig.Type.CLIENT, Config.Client.SPEC);

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
