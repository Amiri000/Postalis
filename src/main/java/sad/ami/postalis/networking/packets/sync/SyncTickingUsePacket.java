package sad.ami.postalis.networking.packets.sync;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.api.PlayerItemInteraction;
import sad.ami.postalis.items.base.interfaces.IHoldTickItem;

public record SyncTickingUsePacket(int tickCount, boolean isTicking) implements CustomPacketPayload {
    public static final Type<SyncTickingUsePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "ticking_use"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncTickingUsePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SyncTickingUsePacket::tickCount,
            ByteBufCodecs.BOOL, SyncTickingUsePacket::isTicking,
            SyncTickingUsePacket::new
    );

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            var player = ctx.player();
            var level = player.getCommandSenderWorld();

            PlayerItemInteraction.useTickCount = tickCount;

            if (isTicking && player.getMainHandItem().getItem() instanceof IHoldTickItem holdTickItem)
                holdTickItem.onHeldTickInMainHand(player, level, tickCount);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
