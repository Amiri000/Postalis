package sad.ami.postalis.networking.packets.sync.animations;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.client.interaction.ClientCastAnimation;

public record CastAnimationPacket(int entityId) implements CustomPacketPayload {
    public static final Type<CastAnimationPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "cast_animations"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CastAnimationPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, CastAnimationPacket::entityId,
            CastAnimationPacket::new
    );

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            var player = Minecraft.getInstance().level.getEntity(entityId);

            if (player == null)
                return;
            Vec3 pos = player.position().add(0, player.getEyeHeight() + 0.6, 0);
            ClientCastAnimation.activeRenders.put(player.getId(), pos);
            //ClientCastAnimation.activeRenders.put(entityId, new FlyingAnimations(player.position().add(0, player.getEyeHeight() + 0.6, 0)));
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
