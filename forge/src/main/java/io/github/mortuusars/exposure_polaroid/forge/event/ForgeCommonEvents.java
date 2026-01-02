package io.github.mortuusars.exposure_polaroid.forge.event;

import io.github.mortuusars.exposure.Exposure;
import io.github.mortuusars.exposure_polaroid.ExposurePolaroid;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ForgeCommonEvents {
    @Mod.EventBusSubscriber(modid = ExposurePolaroid.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBus {

        @SubscribeEvent
        public static void onCreativeTabsBuild(BuildCreativeModeTabContentsEvent event) {
            if (event.getTabKey() == ResourceKey.create(Registries.CREATIVE_MODE_TAB, Exposure.resource("exposure"))) {
                event.accept(ExposurePolaroid.Items.INSTANT_CAMERA.get());
                event.accept(ExposurePolaroid.Items.INSTANT_BLACK_AND_WHITE_SLIDE.get());
                event.accept(ExposurePolaroid.Items.INSTANT_COLOR_SLIDE.get());
                event.accept(ExposurePolaroid.Items.HIGH_SENSITIVITY_INSTANT_BLACK_AND_WHITE_SLIDE.get());
                event.accept(ExposurePolaroid.Items.HIGH_SENSITIVITY_INSTANT_COLOR_SLIDE.get());
            }
        }
    }
}