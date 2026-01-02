package io.github.mortuusars.exposure_polaroid.world.item;

import io.github.mortuusars.exposure.Exposure;
import io.github.mortuusars.exposure.world.camera.ExposureType;
import io.github.mortuusars.exposure.world.camera.film.properties.FilmStyle;
import io.github.mortuusars.exposure.world.item.SensitiveFilmItem;
import io.github.mortuusars.exposure_polaroid.Config;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class InstantSlideItem extends Item implements SensitiveFilmItem {
    protected final ExposureType type;

    final FilmStyle defaultFilmStyle;

    public InstantSlideItem(ExposureType type, Properties properties, FilmStyle defaultFilmStyle) {
        super(properties);
        this.type = type;
        this.defaultFilmStyle = defaultFilmStyle;
    }

    public FilmStyle getOrDefault(ItemStack stack) {
        return Exposure.DataComponents.getFilmStyle(stack,defaultFilmStyle);
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
        return Config.Server.INSTANT_CAMERA_FRAME_SIZE.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, Level context, List<Component> list, TooltipFlag tooltipFlag) {
        addFrameSizeToTooltip(stack, list);

        if (tooltipFlag.isAdvanced()) {
            addPaletteToTooltip(stack, list);
            addStyleToTooltip(stack, list);
        }
    }
}
