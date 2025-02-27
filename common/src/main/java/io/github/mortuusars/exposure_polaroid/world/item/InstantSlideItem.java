package io.github.mortuusars.exposure_polaroid.world.item;

import io.github.mortuusars.exposure.Config;
import io.github.mortuusars.exposure.Exposure;
import io.github.mortuusars.exposure.world.camera.ExposureType;
import io.github.mortuusars.exposure.world.item.FilmItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class InstantSlideItem extends Item implements FilmItem {
    protected final ExposureType type;

    public InstantSlideItem(ExposureType type, Properties properties) {
        super(properties);
        this.type = type;
    }

    @Override
    public ExposureType getType() {
        return type;
    }

    @Override
    public int getDefaultMaxFrameCount(ItemStack stack) {
        return 1;
    }

    @Override
    public int getMaxFrameCount(ItemStack stack) {
        return 1;
    }

    @Override
    public int getDefaultFrameSize(ItemStack stack) {
        //TODO: config
        return 240; /*Config.Server.DEFAULT_FRAME_SIZE.get()*/
    }
}
