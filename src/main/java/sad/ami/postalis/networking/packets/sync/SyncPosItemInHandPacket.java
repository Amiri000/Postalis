package sad.ami.postalis.networking.packets.sync;

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

public record SyncPosItemInHandPacket(Vec3 pos, UUID playerUUID, ItemStack stack) implements CustomPacketPayload {
    public static final Type<SyncPosItemInHandPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "item_in_hand"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncPosItemInHandPacket> STREAM_CODEC = StreamCodec.composite(
            StreamCodecs.VEC3_STREAM_CODEC, SyncPosItemInHandPacket::pos,
            StreamCodecs.UUID_STREAM_CODEC, SyncPosItemInHandPacket::playerUUID,
            ItemStack.STREAM_CODEC, SyncPosItemInHandPacket::stack,
            SyncPosItemInHandPacket::new
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