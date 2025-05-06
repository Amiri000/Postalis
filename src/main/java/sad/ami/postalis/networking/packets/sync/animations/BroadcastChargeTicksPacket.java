package sad.ami.postalis.networking.packets.sync.animations;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.api.ClientCastAnimation;

public record BroadcastChargeTicksPacket(int playerId, int tickCount) implements CustomPacketPayload {
    public static final Type<BroadcastChargeTicksPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "broadcast_charge_ticks"));

    public static final StreamCodec<RegistryFriendlyByteBuf, BroadcastChargeTicksPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, BroadcastChargeTicksPacket::playerId,
            ByteBufCodecs.INT, BroadcastChargeTicksPacket::tickCount,
            BroadcastChargeTicksPacket::new);

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            var level = Minecraft.getInstance().level;

            if (level == null)
                return;

            ClientCastAnimation.putChargeTicks((Player) level.getEntity(playerId), tickCount);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
