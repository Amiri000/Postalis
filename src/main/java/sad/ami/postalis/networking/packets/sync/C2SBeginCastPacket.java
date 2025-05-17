package sad.ami.postalis.networking.packets.sync;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.api.event.PlayerItemInteractionEvent;

public record C2SBeginCastPacket(int tickCount, BlockPos blockPos) implements CustomPacketPayload {
    public static final Type<C2SBeginCastPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "begin_cast"));

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SBeginCastPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, C2SBeginCastPacket::tickCount,
            BlockPos.STREAM_CODEC, C2SBeginCastPacket::blockPos,
            C2SBeginCastPacket::new
    );

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            var player = ctx.player();

            NeoForge.EVENT_BUS.post(new PlayerItemInteractionEvent(player, player.getCommandSenderWorld(), blockPos));
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
