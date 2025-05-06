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

public record CastAnimationPacket(int entityId, boolean start) implements CustomPacketPayload {
    public static final Type<CastAnimationPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "cast_animations"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CastAnimationPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, CastAnimationPacket::entityId,
            ByteBufCodecs.BOOL, CastAnimationPacket::start,
            CastAnimationPacket::new
    );

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            var mc = Minecraft.getInstance();

            if (mc.level == null)
                return;

            ClientCastAnimation.startAnimation((Player) mc.level.getEntity(entityId()));
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
