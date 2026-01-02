package io.github.mortuusars.exposure_polaroid.forge;

import io.github.mortuusars.exposure_polaroid.ExposurePolaroid;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class RegisterImpl {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, ExposurePolaroid.ID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, ExposurePolaroid.ID);

    public static <T extends Item> Supplier<T> item(String id, Supplier<T> supplier) {
        return ITEMS.register(id, supplier);
    }

    public static <T extends SoundEvent> Supplier<T> soundEvent(String id, Supplier<T> supplier) {
        return SOUND_EVENTS.register(id, supplier);
    }
}
