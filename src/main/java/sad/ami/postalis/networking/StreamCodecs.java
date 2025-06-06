package sad.ami.postalis.networking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;
import sad.ami.postalis.client.interaction.ClientCastAnimation;

import java.util.Locale;

public class StreamCodecs {
    public static final StreamCodec<RegistryFriendlyByteBuf, Vec3> VEC3_STREAM_CODEC = StreamCodec.of((buf, vec) -> {
        buf.writeDouble(vec.x);buf.writeDouble(vec.y);buf.writeDouble(vec.z);
    }, buf -> new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientCastAnimation.UseStage> USE_STAGE_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ClientCastAnimation.UseStage::getId,
            id -> ClientCastAnimation.UseStage.BY_ID.getOrDefault(id.toLowerCase(Locale.ROOT), ClientCastAnimation.UseStage.COMPLETED));

    public static <T> StreamCodec<RegistryFriendlyByteBuf, T> emptyStreamCodec(Class<T> clazz) {
        return new StreamCodec<>() {
            @Override
            public void encode(RegistryFriendlyByteBuf buffer, T value) {
            }

            @Override
            public T decode(RegistryFriendlyByteBuf buffer) {
                try {
                    return clazz.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Failed to decode packet: " + clazz.getName(), e);
                }
            }
        };
    }
}
