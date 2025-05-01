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
import org.jetbrains.annotations.NotNull;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.api.PlayerItemInteraction;
import sad.ami.postalis.items.base.IHoldTickItem;
import sad.ami.postalis.items.base.ISwordItem;

import javax.annotation.Nonnull;

@Data
@AllArgsConstructor
public class LastTickUsePacket implements CustomPacketPayload {
    private final int tickCount;

    public static final CustomPacketPayload.Type<LastTickUsePacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "last_tick_use"));

    public static final StreamCodec<RegistryFriendlyByteBuf, LastTickUsePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, LastTickUsePacket::getTickCount,
            LastTickUsePacket::new
    );

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            PlayerItemInteraction.serverTickCount = tickCount;

        });
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
