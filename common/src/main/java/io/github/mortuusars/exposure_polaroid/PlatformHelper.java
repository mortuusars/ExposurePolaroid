package io.github.mortuusars.exposure_polaroid;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class PlatformHelper {
    @ExpectPlatform
    public static boolean isModLoaded(String modId) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isInDevEnv() {
        throw new AssertionError();
    }
}
