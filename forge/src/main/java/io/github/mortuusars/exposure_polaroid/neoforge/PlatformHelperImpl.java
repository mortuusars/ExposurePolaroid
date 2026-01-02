package io.github.mortuusars.exposure_polaroid.neoforge;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.function.Consumer;

public class PlatformHelperImpl {

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    public static boolean isInDevEnv() {
        return !FMLEnvironment.production;
    }
}
