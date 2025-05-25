package sad.ami.postalis.networking.packets.screen;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.client.ClientPlayerHandlers;

public record OpenScreenPacket(BlockPos pedestalPos) implements CustomPacketPayload {
    public static final Type<OpenScreenPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "open_ability_screen"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenScreenPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, OpenScreenPacket::pedestalPos,
            OpenScreenPacket::new);

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> ClientPlayerHandlers.openAbilityScreen(pedestalPos));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
