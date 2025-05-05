package sad.ami.postalis.networking.packets;

import lombok.AllArgsConstructor;
import lombok.Data;
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

@Data
@AllArgsConstructor
public class CastAnimationPacket implements CustomPacketPayload {
    private final int entityId;
    private final boolean start;

    public static final Type<CastAnimationPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "cast_animations"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CastAnimationPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, CastAnimationPacket::getEntityId,
            ByteBufCodecs.BOOL, CastAnimationPacket::isStart,
            CastAnimationPacket::new
    );

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            var mc = Minecraft.getInstance();

            if (mc.level == null)
                return;

            ClientCastAnimation.startAnimation((Player) mc.level.getEntity(getEntityId()));
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
