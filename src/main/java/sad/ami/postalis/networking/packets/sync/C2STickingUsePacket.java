package sad.ami.postalis.networking.packets.sync;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.networking.NetworkHandler;
import sad.ami.postalis.networking.packets.sync.animations.ChargeTicksPacket;

public record C2STickingUsePacket(int playerId, int tickCount) implements CustomPacketPayload {
    public static final Type<C2STickingUsePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "ticking_use"));

    public static final StreamCodec<RegistryFriendlyByteBuf, C2STickingUsePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, C2STickingUsePacket::playerId,
            ByteBufCodecs.INT, C2STickingUsePacket::tickCount,
            C2STickingUsePacket::new
    );

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            var player = ctx.player();
            var level = (ServerLevel) player.getCommandSenderWorld();

             for (var playerA : level.getChunkSource().chunkMap.getPlayers(player.chunkPosition(), false)) {
                if (playerA.getUUID() == player.getUUID())
                    continue;

                NetworkHandler.sendToClient(new ChargeTicksPacket(playerId(), tickCount()), playerA);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
