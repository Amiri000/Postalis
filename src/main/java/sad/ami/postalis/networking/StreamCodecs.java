package sad.ami.postalis.networking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;
import sad.ami.postalis.api.interaction.PlayerInteractionItem;

import java.util.Locale;

public class StreamCodecs {
    public static final StreamCodec<RegistryFriendlyByteBuf, Vec3> VEC3_STREAM_CODEC = StreamCodec.of((buf, vec) -> {
        buf.writeDouble(vec.x);
        buf.writeDouble(vec.y);
        buf.writeDouble(vec.z);
    }, buf -> new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerInteractionItem.UseStage> USE_STAGE_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, PlayerInteractionItem.UseStage::getId,
            id -> PlayerInteractionItem.UseStage.BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), PlayerInteractionItem.UseStage.START)
    );
}
