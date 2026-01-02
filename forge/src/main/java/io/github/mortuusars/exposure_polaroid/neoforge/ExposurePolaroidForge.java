package io.github.mortuusars.exposure_polaroid.neoforge;

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

        RegisterImpl.BLOCK_ENTITY_TYPES.register(modEventBus);
        RegisterImpl.ENTITY_TYPES.register(modEventBus);
        RegisterImpl.ITEMS.register(modEventBus);
        RegisterImpl.MENU_TYPES.register(modEventBus);
        RegisterImpl.RECIPE_SERIALIZERS.register(modEventBus);
        RegisterImpl.SOUND_EVENTS.register(modEventBus);
        RegisterImpl.COMMAND_ARGUMENT_TYPES.register(modEventBus);
        RegisterImpl.WORLD_GEN_FEATURES.register(modEventBus);
        RegisterImpl.PARTICLE_TYPES.register(modEventBus);
        RegisterImpl.CUSTOM_STATS.register(modEventBus);

        if (FMLEnvironment.dist.isClient()) {
            ExposurePolaroidForgeClient.init(modEventBus);
        }
    }
}