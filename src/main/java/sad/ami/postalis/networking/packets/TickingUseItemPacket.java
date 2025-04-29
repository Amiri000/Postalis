package sad.ami.postalis.networking.packets;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.items.base.IHoldTickItem;

@Data
@AllArgsConstructor
public class TickingUseItemPacket implements CustomPacketPayload {
    private final ItemStack stack;
    private final int tickCount;
    private final boolean isTicking;

    public static final Type<TickingUseItemPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "manage_link"));

    public static final StreamCodec<RegistryFriendlyByteBuf, TickingUseItemPacket> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, TickingUseItemPacket::getStack,
            ByteBufCodecs.INT, TickingUseItemPacket::getTickCount,
            ByteBufCodecs.BOOL, TickingUseItemPacket::isTicking,
            TickingUseItemPacket::new
    );

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            var player = ctx.player();
            var item = (IHoldTickItem) stack.getItem();
            var level = player.getCommandSenderWorld();

            if (isTicking) {
                item.onHeldTickInMainHand(player, stack, level, tickCount);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
