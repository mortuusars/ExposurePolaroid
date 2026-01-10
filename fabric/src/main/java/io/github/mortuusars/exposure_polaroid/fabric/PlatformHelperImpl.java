package io.github.mortuusars.exposure_polaroid.fabric;

import net.fabricmc.loader.api.FabricLoader;

public class PlatformHelperImpl {

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public static boolean isInDevEnv() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
