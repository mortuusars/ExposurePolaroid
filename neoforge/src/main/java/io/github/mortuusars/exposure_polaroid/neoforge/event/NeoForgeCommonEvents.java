package io.github.mortuusars.exposure_polaroid.neoforge.event;

import io.github.mortuusars.exposure.network.neoforge.PacketsImpl;
import io.github.mortuusars.exposure.network.packet.Packet;
import io.github.mortuusars.exposure_polaroid.ExposurePolaroid;
import io.github.mortuusars.exposure_polaroid.network.packet.C2SPackets;
import io.github.mortuusars.exposure_polaroid.network.packet.CommonPackets;
import io.github.mortuusars.exposure_polaroid.network.packet.S2CPackets;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NeoForgeCommonEvents {
    @EventBusSubscriber(modid = ExposurePolaroid.ID, bus = EventBusSubscriber.Bus.MOD)
    public static class ModBus {
        @SubscribeEvent
        public static void commonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(ExposurePolaroid.Stats::register);
        }

        @SubscribeEvent
        public static void onCreativeTabsBuild(BuildCreativeModeTabContentsEvent event) {
            if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
                event.accept(ExposurePolaroid.Items.INSTANT_CAMERA.get());
                event.accept(ExposurePolaroid.Items.INSTANT_COLOR_SLIDE.get());
                event.accept(ExposurePolaroid.Items.INSTANT_BLACK_AND_WHITE_SLIDE.get());
            }
        }

        @SuppressWarnings("unchecked")
        @SubscribeEvent
        public static void registerPackets(RegisterPayloadHandlersEvent event) {
            PayloadRegistrar registrar = event.registrar("1");
            // This monstrosity is to avoid having to define packets for forge and fabric separately.
            for (CustomPacketPayload.TypeAndCodec<? extends FriendlyByteBuf, ? extends CustomPacketPayload> definition : S2CPackets.getDefinitions()) {
                registrar.playToClient((CustomPacketPayload.Type<Packet>) definition.type(),
                        (StreamCodec<FriendlyByteBuf, Packet>) definition.codec(), PacketsImpl::handle);
            }

            for (CustomPacketPayload.TypeAndCodec<? extends FriendlyByteBuf, ? extends CustomPacketPayload> definition : C2SPackets.getDefinitions()) {
                registrar.playToServer((CustomPacketPayload.Type<Packet>) definition.type(),
                        (StreamCodec<FriendlyByteBuf, Packet>) definition.codec(), PacketsImpl::handle);
            }

            for (CustomPacketPayload.TypeAndCodec<? extends FriendlyByteBuf, ? extends CustomPacketPayload> definition : CommonPackets.getDefinitions()) {
                registrar.playBidirectional((CustomPacketPayload.Type<Packet>) definition.type(),
                        (StreamCodec<FriendlyByteBuf, Packet>) definition.codec(), PacketsImpl::handle);
            }
        }
    }

    @EventBusSubscriber(modid = ExposurePolaroid.ID, bus = EventBusSubscriber.Bus.GAME)
    public static class ForgeBus {
        @SubscribeEvent
        public static void registerCommands(RegisterCommandsEvent event) {
        }
    }
}