package sad.ami.postalis.networking;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.networking.packets.TickingUseItemPacket;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class NetworkHandler {
    @SubscribeEvent
    public static void onRegisterPayloadHandler(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(Postalis.MODID).versioned("1.0").optional();

        registrar.playToServer(TickingUseItemPacket.TYPE, TickingUseItemPacket.STREAM_CODEC, TickingUseItemPacket::handle);
    }

    public static <T extends CustomPacketPayload> void sendToServer(T message) {
        PacketDistributor.sendToServer(message);
    }

    public static <T extends CustomPacketPayload> void sendToClient(T message, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, message);
    }

}
