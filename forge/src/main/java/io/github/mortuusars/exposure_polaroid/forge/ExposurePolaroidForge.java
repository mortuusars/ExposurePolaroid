package io.github.mortuusars.exposure_polaroid.forge;

import com.google.common.base.Preconditions;
import io.github.mortuusars.exposure_polaroid.Config;
import io.github.mortuusars.exposure_polaroid.ExposurePolaroid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.Nullable;

@Mod(ExposurePolaroid.ID)
public class ExposurePolaroidForge {
    public ExposurePolaroidForge() {
        ExposurePolaroid.init();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.Server.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.Client.SPEC);

        @Nullable IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Preconditions.checkNotNull(modEventBus);

        RegisterImpl.ITEMS.register(modEventBus);
        RegisterImpl.SOUND_EVENTS.register(modEventBus);

        if (FMLEnvironment.dist.isClient()) {
            ExposurePolaroidForgeClient.init(modEventBus);
        }
    }
}