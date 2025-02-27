package io.github.mortuusars.exposure_polaroid.world.item;

import io.github.mortuusars.exposure.data.ColorPalette;
import io.github.mortuusars.exposure.data.ColorPalettes;
import io.github.mortuusars.exposure.world.camera.ExposureType;
import io.github.mortuusars.exposure.world.item.FilmItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

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

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        int frameSize = getFrameSize(stack);
        if (frameSize != getDefaultFrameSize(stack)) {
            tooltipComponents.add(Component.translatable("item.exposure.film_roll.tooltip.frame_size",
                            Component.literal(String.format("%.1f", frameSize / 10f)))
                    .withStyle(ChatFormatting.GRAY));
        }

        ResourceKey<ColorPalette> colorPaletteId = getColorPaletteId(stack);
        if (tooltipFlag.isAdvanced() && !colorPaletteId.equals(ColorPalettes.DEFAULT)) {
            tooltipComponents.add(Component.translatable("item.exposure.film_roll.tooltip.palette", colorPaletteId.location().toString())
                    .withStyle(ChatFormatting.GRAY));
        }
    }
}
