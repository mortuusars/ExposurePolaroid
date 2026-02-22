package io.github.mortuusars.exposure_polaroid.fabric;

import io.github.mortuusars.exposure_polaroid.ExposurePolaroid;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class RegisterImpl {

    public static <T extends Item> Supplier<T> item(String id, Supplier<T> supplier) {
        T obj = Registry.register(BuiltInRegistries.ITEM, ExposurePolaroid.resource(id), supplier.get());
        return () -> obj;
    }

    public static <T extends SoundEvent> Supplier<T> soundEvent(String id, Supplier<T> supplier) {
        T obj = Registry.register(BuiltInRegistries.SOUND_EVENT, ExposurePolaroid.resource(id), supplier.get());
        return () -> obj;
    }
}
