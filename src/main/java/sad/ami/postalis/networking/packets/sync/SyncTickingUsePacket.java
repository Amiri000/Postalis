package sad.ami.postalis.networking.packets.sync;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.api.event.PlayerItemInteractionEvent;
import sad.ami.postalis.client.interaction.ClientCastAnimation;
import sad.ami.postalis.networking.NetworkHandler;
import sad.ami.postalis.networking.StreamCodecs;
import sad.ami.postalis.networking.packets.sync.animations.ChargeTicksPacket;

public record SyncTickingUsePacket(int tickCount, ClientCastAnimation.UseStage stage) implements CustomPacketPayload {
    public static final Type<SyncTickingUsePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "ticking_use"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncTickingUsePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SyncTickingUsePacket::tickCount,
            StreamCodecs.USE_STAGE_STREAM_CODEC, SyncTickingUsePacket::stage,
            SyncTickingUsePacket::new
    );

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            var player = ctx.player();
            var level = (ServerLevel) player.getCommandSenderWorld();

            NeoForge.EVENT_BUS.post(new PlayerItemInteractionEvent(player, level, stage, tickCount));

            for (var playerA : level.getChunkSource().chunkMap.getPlayers(player.chunkPosition(), false)) {
                if (playerA.getUUID() == player.getUUID())
                    continue;

                NetworkHandler.sendToClient(new ChargeTicksPacket(player.getId(), tickCount()), playerA);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
