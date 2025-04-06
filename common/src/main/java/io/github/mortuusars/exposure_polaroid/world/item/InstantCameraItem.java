package io.github.mortuusars.exposure_polaroid.world.item;

import io.github.mortuusars.exposure.Exposure;
import io.github.mortuusars.exposure.client.util.Minecrft;
import io.github.mortuusars.exposure.data.Lenses;
import io.github.mortuusars.exposure.server.CameraInstances;
import io.github.mortuusars.exposure.util.ExtraData;
import io.github.mortuusars.exposure.world.camera.CameraId;
import io.github.mortuusars.exposure.world.camera.capture.CaptureParameters;
import io.github.mortuusars.exposure.world.camera.component.FocalRange;
import io.github.mortuusars.exposure.world.camera.component.ShutterSpeed;
import io.github.mortuusars.exposure.world.camera.frame.Frame;
import io.github.mortuusars.exposure.world.entity.CameraHolder;
import io.github.mortuusars.exposure.world.entity.CameraStandEntity;
import io.github.mortuusars.exposure.world.item.PhotographItem;
import io.github.mortuusars.exposure.world.item.StackedPhotographsItem;
import io.github.mortuusars.exposure.world.item.camera.Attachment;
import io.github.mortuusars.exposure.world.item.camera.CameraItem;
import io.github.mortuusars.exposure.world.item.camera.Flash;
import io.github.mortuusars.exposure.world.item.camera.Shutter;
import io.github.mortuusars.exposure_polaroid.Config;
import io.github.mortuusars.exposure_polaroid.ExposurePolaroid;
import io.github.mortuusars.exposure_polaroid.world.camera.PolaroidFrameExtraData;
import io.github.mortuusars.exposure_polaroid.world.item.camera.InstantCameraAttachment;
import io.github.mortuusars.exposure_polaroid.world.item.camera.InstantCameraShutter;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InstantCameraItem extends CameraItem {
    public static final ResourceLocation CAPTURE_TYPE = ExposurePolaroid.resource("instant_camera");

    public InstantCameraItem(Properties properties) {
        super(properties);
    }

    @Override
    protected Shutter createShutter() {
        return new InstantCameraShutter();
    }

    @Override
    protected Flash createFlash() {
        return new Flash() {
            @Override
            public boolean isAvailable(ItemStack camera) {
                return true;
            }
        };
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

    protected @NotNull List<Attachment<?>> defineAttachments() {
        return List.of(InstantCameraAttachment.INSTANT_SLIDE);
    }

    @Override
    public Attachment<?> getFilmAttachment() {
        return InstantCameraAttachment.INSTANT_SLIDE;
    }

    @Override
    public boolean hasAttachmentsMenu() {
        return false;
    }

    // --

    @Override
    public ResourceLocation getCaptureType(ItemStack stack) {
        return CAPTURE_TYPE;
    }

    @Override
    public float getScaleOnStand() {
        return 0.85f;
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
    public FocalRange getFocalRange(RegistryAccess registryAccess, ItemStack stack) {
        // Even though you can't install lens in survival, it can be added as a component to create camera with custom focal range.
        if (!Attachment.LENS.isEmpty(stack)) {
            return Attachment.LENS.map(stack, lensStack -> Lenses.getFocalRange(registryAccess, lensStack)
                            .orElse(FocalRange.parse(Config.Server.INSTANT_CAMERA_FOCAL_RANGE.get())))
                    .orElse(FocalRange.parse(Config.Server.INSTANT_CAMERA_FOCAL_RANGE.get()));
        }
        return FocalRange.parse(Config.Server.INSTANT_CAMERA_FOCAL_RANGE.get());
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
        return InstantCameraAttachment.INSTANT_SLIDE.maxCount().get();
    }

    public int getRemainingSlides(ItemStack stack) {
        return InstantCameraAttachment.INSTANT_SLIDE.get(stack).getForReading().getCount();
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
        float f = Math.max(0.0F, ((float) slide.getCount()) / (float) max);
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
            if (stack.getEntityRepresentation() instanceof CameraStandEntity) {
                if (Screen.hasShiftDown()) {
                    components.add(Component.translatable("item.exposure_polaroid.instant_camera.tooltip.details_insert_on_stand"));
                } else
                    components.add(Component.translatable("tooltip.exposure.hold_for_details"));
                return;
            }

            if (Screen.hasShiftDown()) {
                components.add(Component.translatable("item.exposure_polaroid.instant_camera.tooltip.details_insert"));
            } else {
                components.add(Component.translatable("tooltip.exposure.hold_for_details"));
            }
        }
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack otherStack, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action != ClickAction.SECONDARY || !slot.allowModification(player)) return false;

        if (getShutter().isOpen(stack) || isOnCooldown(player, stack)) return true;

        InteractionResult hotswap = hotswap(player, stack, otherStack, access);
        if (hotswap == InteractionResult.FAIL) return true;
        if (hotswap.consumesAction()) {
            // For some unknown reason, inserting slides when camera already has some does not sync it to the server.
            // But only if the player is in "inventory" tab of creative inventory.
            // This fixes it:
            if (player.isCreative()) {
                Minecrft.gameMode().handleCreativeModeItemAdd(stack, slot.index);
            }
            return true;
        }

        return false;
    }

    // --

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
    public InteractionResult handleStandSneakInteraction(CameraStandEntity stand, Player player, InteractionHand hand, ItemStack cameraStack) {
        return super.handleStandSneakInteraction(stand, player, hand, cameraStack);
    }

    @Override
    public boolean canTakePhoto(CameraHolder holder, ItemStack stack) {
        return !isOnCooldown(holder, stack)
                && !getTimer().isTicking(holder, stack)
                && !getShutter().isOpen(stack)
                && !InstantCameraAttachment.INSTANT_SLIDE.isEmpty(stack)
                && CameraInstances.canReleaseShutter(CameraId.ofStack(stack));
    }

    @Override
    public int calculateCooldownAfterShot(ItemStack stack, CaptureParameters captureParameters) {
        return 40;
    }

    @Override
    public boolean tick(CameraHolder holder, ItemStack stack) {
        boolean changed = super.tick(holder, stack);

        @Nullable Frame frame = stack.get(Exposure.DataComponents.PHOTOGRAPH_FRAME);
        if (frame != null && !getShutter().isOpen(stack) && !isOnCooldown(holder, stack)) {
            stack.remove(Exposure.DataComponents.PHOTOGRAPH_FRAME);
            printPhotograph(holder, stack, frame);
            return true;
        }

        return changed;
    }

    protected void printPhotograph(CameraHolder holder, ItemStack stack, Frame frame) {
        Entity holderEntity = holder.asHolderEntity();
        Level level = holderEntity.level();

        ItemStack photograph = new ItemStack(Exposure.Items.PHOTOGRAPH.get());
        photograph.set(Exposure.DataComponents.PHOTOGRAPH_FRAME, frame);

        if (holder instanceof Player player) {
            if (!player.isCreative()) {
                ItemStack slide = InstantCameraAttachment.INSTANT_SLIDE.get(stack).getCopy();
                slide.shrink(1);
                slide = slide.isEmpty() ? ItemStack.EMPTY : slide;
                InstantCameraAttachment.INSTANT_SLIDE.set(stack, slide);
            }

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
                    itemEntity.setPickUpDelay(5);
                }
            }
        } else {
            ItemStack slide = InstantCameraAttachment.INSTANT_SLIDE.get(stack).getCopy();
            slide.shrink(1);
            slide = slide.isEmpty() ? ItemStack.EMPTY : slide;
            InstantCameraAttachment.INSTANT_SLIDE.set(stack, slide);

            if (!photograph.isEmpty() && !level.isClientSide()) {
                Vec3 look = holderEntity.getLookAngle().scale(0.2f);
                Vec3 pos = holderEntity.getEyePosition();
                ItemEntity itemEntity = new ItemEntity(level,
                        pos.x + look.x * 2, pos.y + look.y * 2 - 0.3f, pos.z + look.z * 2, photograph,
                        look.x * 0.5, look.y * 0.5, look.z * 0.5);
                itemEntity.setPickUpDelay(5);
                level.addFreshEntity(itemEntity);
            }
        }

        holder.getServerPlayerAwardedForExposure().ifPresent(serverPlayer ->
                Exposure.CriteriaTriggers.FRAME_PRINTED.get().trigger(
                        serverPlayer, holderEntity.blockPosition(), frame, photograph));

        if (!level.isClientSide) {
            level.playSound(null, holderEntity, Exposure.SoundEvents.PHOTOGRAPH_RUSTLE.get(), SoundSource.PLAYERS, 0.6f,
                    level.getRandom().nextFloat() * 0.2f + 1.2f);
        }
    }

    @Override
    protected void addFrameExtraData(CameraHolder holder, ServerLevel level, ItemStack camera, CaptureParameters captureParameters, List<BlockPos> positionsInFrame, List<LivingEntity> entitiesInFrame, ExtraData data) {
        data.put(PolaroidFrameExtraData.INSTANT, true);
        super.addFrameExtraData(holder, level, camera, captureParameters, positionsInFrame, entitiesInFrame, data);
    }

    @Override
    public void addFrameToFilm(ItemStack stack, Frame frame) {
        stack.set(Exposure.DataComponents.PHOTOGRAPH_FRAME, frame);
    }
}