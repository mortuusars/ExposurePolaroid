package io.github.mortuusars.exposure_polaroid.world.item;

import io.github.mortuusars.exposure.Exposure;
import io.github.mortuusars.exposure.ExposureServer;
import io.github.mortuusars.exposure.data.ColorPalette;
import io.github.mortuusars.exposure.data.ColorPalettes;
import io.github.mortuusars.exposure.data.Lenses;
import io.github.mortuusars.exposure.network.Packets;
import io.github.mortuusars.exposure.network.packet.clientbound.CaptureStartS2CP;
import io.github.mortuusars.exposure.server.CameraInstances;
import io.github.mortuusars.exposure.util.ExtraData;
import io.github.mortuusars.exposure.world.camera.CameraId;
import io.github.mortuusars.exposure.world.camera.capture.CaptureProperties;
import io.github.mortuusars.exposure.world.camera.capture.ProjectionInfo;
import io.github.mortuusars.exposure.world.camera.component.FocalRange;
import io.github.mortuusars.exposure.world.camera.component.ShutterSpeed;
import io.github.mortuusars.exposure.world.camera.frame.Frame;
import io.github.mortuusars.exposure.world.entity.CameraHolder;
import io.github.mortuusars.exposure.world.item.FilmItem;
import io.github.mortuusars.exposure.world.item.PhotographItem;
import io.github.mortuusars.exposure.world.item.StackedPhotographsItem;
import io.github.mortuusars.exposure.world.item.camera.Attachment;
import io.github.mortuusars.exposure.world.item.camera.CameraItem;
import io.github.mortuusars.exposure.world.item.camera.CameraSettings;
import io.github.mortuusars.exposure.world.item.camera.Shutter;
import io.github.mortuusars.exposure.world.item.component.StoredItemStack;
import io.github.mortuusars.exposure.world.item.util.ItemAndStack;
import io.github.mortuusars.exposure.world.level.LevelUtil;
import io.github.mortuusars.exposure.world.level.storage.ExposureIdentifier;
import io.github.mortuusars.exposure.world.sound.Sound;
import io.github.mortuusars.exposure_polaroid.Config;
import io.github.mortuusars.exposure_polaroid.ExposurePolaroid;
import io.github.mortuusars.exposure_polaroid.world.camera.PolaroidFrameExtraData;
import io.github.mortuusars.exposure_polaroid.world.item.camera.InstantCameraAttachment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class InstantCameraItem extends CameraItem {
    public static final ResourceLocation CAPTURE_TYPE = ExposurePolaroid.resource("instant_camera");

    public InstantCameraItem(Shutter shutter, Properties properties) {
        super(shutter, properties);
    }

    protected @NotNull List<Attachment<?>> defineAttachments() {
        return List.of(InstantCameraAttachment.INSTANT_SLIDE);
    }

    protected List<ShutterSpeed> defineShutterSpeeds() {
        return List.of(
                new ShutterSpeed("1/125"),
                new ShutterSpeed("1/103"),
                new ShutterSpeed("1/81"),
                new ShutterSpeed("1/60"),
                new ShutterSpeed("1/41"),
                new ShutterSpeed("1/30"),
                new ShutterSpeed("1/20")
        );
    }

    // --

    @Override
    public ResourceLocation getCaptureType(ItemStack stack) {
        return CAPTURE_TYPE;
    }

    @Override
    public SoundEvent getViewfinderOpenSound() {
        return ExposurePolaroid.SoundEvents.INSTANT_CAMERA_VIEWFINDER_OPEN.get();
    }

    @Override
    public SoundEvent getViewfinderCloseSound() {
        return ExposurePolaroid.SoundEvents.INSTANT_CAMERA_VIEWFINDER_CLOSE.get();
    }

    @Override
    protected Optional<ProjectionInfo> getProjectionInfo(ItemStack stack) {
        return Optional.empty();
    }

    @Override
    public Holder<ColorPalette> getColorPalette(RegistryAccess registryAccess, ItemStack stack) {
        ResourceKey<ColorPalette> key = InstantCameraAttachment.INSTANT_SLIDE.map(stack, FilmItem::getColorPaletteId)
                .orElse(ColorPalettes.DEFAULT);
        return ColorPalettes.get(registryAccess, key);
    }

    @Override
    public FocalRange getFocalRange(RegistryAccess registryAccess, ItemStack stack) {
        if (!Attachment.LENS.isEmpty(stack)) {
            return Attachment.LENS.map(stack, lensStack -> Lenses.getFocalRangeOrDefault(registryAccess, lensStack))
                    .orElse(new FocalRange(20, 40));
        }
        return new FocalRange(20, 40);
    }

    @Override
    public double getYPositionOffset(ItemStack stack) {
        return 0;
    }

    @Override
    public float getCropFactor() {
        return 0.75f;
    }

    public int getMaxSlideCount() {
        return 12;
    }

    public int getRemainingSlides(ItemStack stack) {
        return InstantCameraAttachment.INSTANT_SLIDE.get(stack).getForReading().getCount();
    }

    @Override
    public boolean hasFlash(ItemStack stack) {
        return true;
    }

    // --

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return Config.Client.INSTANT_CAMERA_SHOW_FULLNESS_BAR_ON_ITEM.get() && !InstantCameraAttachment.INSTANT_SLIDE.isEmpty(stack);
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        return InstantCameraAttachment.INSTANT_SLIDE.map(stack, this::getSlideFullness).orElse(0);
    }

    protected int getSlideFullness(ItemStack stack) {
        return Math.min(1 + 12 * stack.getCount() / getMaxSlideCount(), 13);
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        ItemStack slide = InstantCameraAttachment.INSTANT_SLIDE.get(stack).getForReading();
        int max = getMaxSlideCount();
        float f = Math.max(0.0F, ((float)slide.getCount()) / (float)max);
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }

    // --

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
        if (Config.Client.INSTANT_CAMERA_SHOW_SLIDES_COUNT_IN_TOOLTIP.get()) {
            InstantCameraAttachment.INSTANT_SLIDE.ifPresent(stack, (slideItem, slideStack) -> {
                int exposed = slideStack.getCount();
                int max = getMaxSlideCount();
                components.add(Component.translatable("item.exposure_polaroid.instant_camera.tooltip.slides", exposed, max));
            });
        }

        if (Config.Client.INSTANT_CAMERA_SHOW_TOOLTIP_DETAILS.get()) {
            if (Screen.hasShiftDown()) {
                components.add(Component.translatable("item.exposure_polaroid.instant_camera.tooltip.details_insert"));
            } else {
                components.add(Component.translatable("tooltip.exposure.hold_for_details"));
            }
        }
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack otherStack, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action != ClickAction.SECONDARY) return false;

        if (getShutter().isOpen(stack)) {
            player.playSound(Exposure.SoundEvents.CAMERA_LENS_RING_CLICK.get(), 0.9f, 1f);
            player.displayClientMessage(Component.translatable("item.exposure.camera.camera_attachments.fail.shutter_open")
                    .withStyle(ChatFormatting.RED), true);
            return true;
        }

        StoredItemStack slideStack = InstantCameraAttachment.INSTANT_SLIDE.get(stack);

        if (InstantCameraAttachment.INSTANT_SLIDE.matches(otherStack)) {
            ItemStack stored = slideStack.getForReading();
            int availableSlots = Math.max(0, getMaxSlideCount() - stored.getCount());

            if (availableSlots == 0 || (!stored.isEmpty() && !ItemStack.isSameItemSameComponents(stored, otherStack))) {
                player.playSound(Exposure.SoundEvents.CAMERA_LENS_RING_CLICK.get(), 0.9f, 1f);
                return true;
            }

            ItemStack insertedStack = otherStack.split(availableSlots);
            insertedStack.setCount(insertedStack.getCount() + stored.getCount());
            InstantCameraAttachment.INSTANT_SLIDE.set(stack, insertedStack);

            access.set(otherStack);
            InstantCameraAttachment.INSTANT_SLIDE.playInsertSoundSided(player);

            return true;
        }

        if (otherStack.isEmpty() && !slideStack.isEmpty()) {
            access.set(slideStack.getCopy());
            InstantCameraAttachment.INSTANT_SLIDE.set(stack, ItemStack.EMPTY);
            player.level().playSound(player, player, ExposurePolaroid.SoundEvents.INSTANT_CAMERA_VIEWFINDER_OPEN.get(),
                    SoundSource.PLAYERS, 0.9f, 1f);
            return true;
        }

        return false;
    }

    @Override
    public int calculateCooldownAfterShot(ItemStack stack, CaptureProperties captureProperties) {
        return 40;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (!(entity instanceof Player player)) return;

        @Nullable Frame frame = stack.get(Exposure.DataComponents.PHOTOGRAPH_FRAME);
        if (!player.getCooldowns().isOnCooldown(stack.getItem()) && frame != null) {
            stack.remove(Exposure.DataComponents.PHOTOGRAPH_FRAME);
            printPhotograph(stack, level, player, frame);
        }
    }

    private static void printPhotograph(ItemStack stack, Level level, Player player, Frame frame) {
        ItemStack slide = InstantCameraAttachment.INSTANT_SLIDE.get(stack).getCopy();
        slide.shrink(1);
        slide = slide.isEmpty() ? ItemStack.EMPTY : slide;
        InstantCameraAttachment.INSTANT_SLIDE.set(stack, slide);

        ItemStack photograph = new ItemStack(Exposure.Items.PHOTOGRAPH.get());
        photograph.set(Exposure.DataComponents.PHOTOGRAPH_FRAME, frame);

        boolean placedInInventory = false;

        for (int slot = 0; slot < player.getInventory().items.size(); slot++) {
            ItemStack item = player.getInventory().getItem(slot);
            if (item.isEmpty()) {
                player.getInventory().setItem(slot, photograph);
                photograph.setPopTime(Inventory.POP_TIME_DURATION);
                placedInInventory = true;
            } else if (item.getItem() instanceof PhotographItem) {
                StackedPhotographsItem stackedPhotographsItem = Exposure.Items.STACKED_PHOTOGRAPHS.get();
                ItemStack stackedPhotographsStack = new ItemStack(stackedPhotographsItem);

                stackedPhotographsItem.addPhotographOnTop(stackedPhotographsStack, item);
                stackedPhotographsItem.addPhotographOnTop(stackedPhotographsStack, photograph);

                player.getInventory().setItem(slot, stackedPhotographsStack);
                stackedPhotographsStack.setPopTime(Inventory.POP_TIME_DURATION);

                placedInInventory = true;
            } else if (item.getItem() instanceof StackedPhotographsItem stackedPhotographs && stackedPhotographs.canAddPhotograph(item)) {
                stackedPhotographs.addPhotographOnTop(item, photograph);
                item.setPopTime(Inventory.POP_TIME_DURATION);
                placedInInventory = true;
            }

            if (placedInInventory) {
                level.playSound(player, player, Exposure.SoundEvents.PHOTOGRAPH_RUSTLE.get(), SoundSource.PLAYERS,
                        0.6f, level.getRandom().nextFloat() * 0.2f + 0.9f);
                break;
            }
        }

        if (!level.isClientSide && !placedInInventory) {
            @Nullable ItemEntity itemEntity = player.drop(photograph, true, false);
            if (itemEntity != null) {
                itemEntity.setNoPickUpDelay();
            }

            level.playSound(null, player, Exposure.SoundEvents.PHOTOGRAPH_RUSTLE.get(), SoundSource.PLAYERS, 0.6f,
                    level.getRandom().nextFloat() * 0.2f + 1.2f);
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (hand == InteractionHand.MAIN_HAND
                && player.getOffhandItem().getItem() instanceof CameraItem offhandCameraItem
                && offhandCameraItem.isActive(player.getOffhandItem())) {
            return InteractionResultHolder.pass(stack);
        }

        if (!isActive(stack)) {
            return activateInHand(player, stack, hand);
        }

        return release(player, stack);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> release(CameraHolder holder, ItemStack stack) {
        Entity entity = holder.asEntity();
        Level level = entity.level();

        Sound.playSided(entity, getReleaseButtonSound(), entity.getSoundSource(), 0.3f, 1f, 0.1f);

        if (level.isClientSide
                || getShutter().isOpen(stack)
                || InstantCameraAttachment.INSTANT_SLIDE.isEmpty(stack)
                || !CameraInstances.canReleaseShutter(CameraId.ofStack(stack))) {
            return InteractionResultHolder.consume(stack);
        }

        ItemAndStack<InstantSlideItem> film = InstantCameraAttachment.INSTANT_SLIDE.get(stack).getItemAndStackCopy();

        if (level instanceof ServerLevel serverLevel) {
            if (!(holder.getPlayerExecutingExposure() instanceof ServerPlayer serverPlayer)) {
                Exposure.LOGGER.error("Cannot start capture: photographer '{}' does not have valid executing player.", holder);
                return InteractionResultHolder.consume(stack);
            }

            int lightLevel = LevelUtil.getLightLevelAt(level, entity.blockPosition());
            boolean shouldFlashFire = shouldFlashFire(stack, lightLevel);
            ShutterSpeed shutterSpeed = CameraSettings.SHUTTER_SPEED.getOrDefault(stack);

            getShutter().open(holder, serverLevel, stack, shutterSpeed);

            boolean flashHasFired = shouldFlashFire && tryUseFlash(entity, serverLevel, stack);

            CameraId cameraId = getOrCreateID(stack);
            String exposureId = ExposureIdentifier.createId(serverPlayer);

            CaptureProperties captureProperties = new CaptureProperties.Builder(exposureId)
                    .setCameraHolder(holder)
                    .setCameraID(cameraId)
                    .setShutterSpeed(CameraSettings.SHUTTER_SPEED.getOrDefault(stack))
                    .setFilmType(film.getItem().getType())
                    .setFrameSize(film.getItem().getFrameSize(film.getItemStack()))
                    .setFovOverride(getFov(level, stack))
                    .setCropFactor(getCropFactor())
                    .setColorPalette(getColorPalette(level.registryAccess(), stack))
                    .setFlash(flashHasFired)
                    .setChromaticChannel(getChromaticChannel(stack))
                    .extraData(tag -> tag.putInt("light_level", lightLevel))
                    .build();

            CameraInstances.createOrUpdate(cameraId, instance -> {
                int cooldown = calculateCooldownAfterShot(stack, captureProperties);
                instance.setDeferredCooldown(cooldown);
            });

            addNewFrame(serverLevel, holder, stack, captureProperties);

            ExposureServer.exposureRepository().expect(serverPlayer, exposureId);
            Packets.sendToClient(new CaptureStartS2CP(getCaptureType(stack), captureProperties), serverPlayer);
        }

        return InteractionResultHolder.consume(stack);
    }

    @Override
    protected void addFrameExtraData(CameraHolder holder, ServerLevel level, ItemStack camera, CaptureProperties captureProperties, List<BlockPos> positionsInFrame, List<LivingEntity> entitiesInFrame, ExtraData data) {
        data.put(PolaroidFrameExtraData.INSTANT, true);
        super.addFrameExtraData(holder, level, camera, captureProperties, positionsInFrame, entitiesInFrame, data);
    }

    @Override
    public void addFrameToFilm(ItemStack stack, Frame frame) {
        stack.set(Exposure.DataComponents.PHOTOGRAPH_FRAME, frame);
    }
}