package sad.ami.postalis.networking;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.networking.packets.sync.C2STickingUsePacket;
import sad.ami.postalis.networking.packets.sync.animations.ChargeTicksPacket;
import sad.ami.postalis.networking.packets.sync.animations.CastAnimationPacket;
import sad.ami.postalis.networking.packets.sync.S2CPosItemInHandPacket;
import sad.ami.postalis.networking.packets.sync.C2SBeginCastPacket;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class NetworkHandler {
    @SubscribeEvent
    public static void onRegisterPayloadHandler(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(Postalis.MODID).versioned("1.0").optional();

        registrar.playToServer(C2STickingUsePacket.TYPE, C2STickingUsePacket.STREAM_CODEC, C2STickingUsePacket::handle);
        registrar.playToServer(C2SBeginCastPacket.TYPE, C2SBeginCastPacket.STREAM_CODEC, C2SBeginCastPacket::handle);
        registrar.playToServer(S2CPosItemInHandPacket.TYPE, S2CPosItemInHandPacket.STREAM_CODEC, S2CPosItemInHandPacket::handle);

        registrar.playToClient(CastAnimationPacket.TYPE, CastAnimationPacket.STREAM_CODEC, CastAnimationPacket::handle);
        registrar.playToClient(ChargeTicksPacket.TYPE, ChargeTicksPacket.STREAM_CODEC, ChargeTicksPacket::handle);
    }

    public static <T extends CustomPacketPayload> void sendToServer(T message) {
        PacketDistributor.sendToServer(message);
    }

    public static <T extends CustomPacketPayload> void sendToClient(T message, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, message);
    }
}
