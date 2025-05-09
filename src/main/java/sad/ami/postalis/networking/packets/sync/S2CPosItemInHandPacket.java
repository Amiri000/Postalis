package sad.ami.postalis.networking.packets.sync;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.networking.StreamCodecs;

import java.util.UUID;

public record S2CPosItemInHandPacket(Vec3 pos, UUID playerUUID, ItemStack stack) implements CustomPacketPayload {
    public static final Type<S2CPosItemInHandPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "item_in_hand"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CPosItemInHandPacket> STREAM_CODEC = StreamCodec.composite(
            StreamCodecs.VEC3_STREAM_CODEC, S2CPosItemInHandPacket::pos,
            UUIDUtil.STREAM_CODEC, S2CPosItemInHandPacket::playerUUID,
            ItemStack.STREAM_CODEC, S2CPosItemInHandPacket::stack,
            S2CPosItemInHandPacket::new
    );

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            var player = ctx.player();
            var level = player.getCommandSenderWorld();


        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}