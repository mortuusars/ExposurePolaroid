package io.github.mortuusars.exposure_polaroid.fabric;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import io.github.mortuusars.exposure.Exposure;
import io.github.mortuusars.exposure_polaroid.Config;
import io.github.mortuusars.exposure_polaroid.ExposurePolaroid;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.fml.config.ModConfig;

public class ExposurePolaroidFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ExposurePolaroid.init();

        ForgeConfigRegistry.INSTANCE.register(ExposurePolaroid.ID, ModConfig.Type.SERVER, Config.Server.SPEC);
        ForgeConfigRegistry.INSTANCE.register(ExposurePolaroid.ID, ModConfig.Type.CLIENT, Config.Client.SPEC);

        ResourceKey<CreativeModeTab> exposureTabKey = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Exposure.resource("exposure"));

        ItemGroupEvents.modifyEntriesEvent(exposureTabKey).register(content -> {
            content.accept(ExposurePolaroid.Items.INSTANT_CAMERA.get());
            content.accept(ExposurePolaroid.Items.INSTANT_BLACK_AND_WHITE_SLIDE.get());
            content.accept(ExposurePolaroid.Items.INSTANT_COLOR_SLIDE.get());
            content.accept(ExposurePolaroid.Items.HIGH_SENSITIVITY_INSTANT_BLACK_AND_WHITE_SLIDE.get());
            content.accept(ExposurePolaroid.Items.HIGH_SENSITIVITY_INSTANT_COLOR_SLIDE.get());
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
        });
    }
}
