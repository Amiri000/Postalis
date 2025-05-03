package sad.ami.postalis.networking.packets.sync;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.api.PlayerItemInteraction;
import sad.ami.postalis.items.base.IHoldTickItem;

public record SyncTickingUsePacket(ItemStack stack, int tickCount, boolean isTicking) implements CustomPacketPayload {
    public static final Type<SyncTickingUsePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "ticking_use"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncTickingUsePacket> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, SyncTickingUsePacket::stack,
            ByteBufCodecs.INT, SyncTickingUsePacket::tickCount,
            ByteBufCodecs.BOOL, SyncTickingUsePacket::isTicking,
            SyncTickingUsePacket::new
    );

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            var player = ctx.player();
            var item = (IHoldTickItem) stack.getItem();
            var level = player.getCommandSenderWorld();

            PlayerItemInteraction.serverTickCount = tickCount;

            item.onHeldTickInMainHand(player, stack, level, tickCount);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
