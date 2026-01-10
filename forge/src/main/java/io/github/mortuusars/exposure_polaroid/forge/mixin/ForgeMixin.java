package io.github.mortuusars.exposure_polaroid.forge.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.logging.LogUtils;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = DatagenModLoader.class, remap = false)
public abstract class ForgeMixin {

    @WrapOperation(
            method = "begin", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraftforge/data/event/GatherDataEvent$DataGeneratorConfig;runAll()V"
    )
    )
    private static void begin(GatherDataEvent.DataGeneratorConfig dataGeneratorConfig, Operation<Void> operation) {
        // Architectury Loom does not exit the data run configuration, this will allow it to do so
        if (!FMLEnvironment.production && isRunningDataGen()) {
            try {
                operation.call(dataGeneratorConfig);
            } catch (Throwable throwable) {
                LogUtils.getLogger().error("Data generation failed", throwable);
            } finally {
                System.exit(0);
            }
        } else {
            operation.call(dataGeneratorConfig);
        }
    }

    @Shadow
    private static boolean isRunningDataGen() {
        throw new RuntimeException();
    }
}
