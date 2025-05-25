package sad.ami.postalis.items.base;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum BranchType {
    NONE("none", Component.translatable("branch_type.postalis.none")),

    WIND("wind", Component.translatable("branch_type.postalis.wind")),
    STORM("storm", Component.translatable("branch_type.postalis.storm"));

    private final String id;
    private final MutableComponent displayName;

    public static final Codec<BranchType> CODEC = Codec.STRING.flatXmap(id -> Arrays.stream(values())
            .filter(type -> type.id.equals(id))
            .findFirst()
            .map(DataResult::success)
            .orElseGet(() -> DataResult.error(() -> "Unknown BranchType id: " + id)), type -> DataResult.success(type.getId()));

    public static final StreamCodec<ByteBuf, BranchType> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(id -> Arrays.stream(values())
            .filter(t -> t.id.equals(id))
            .findFirst()
            .orElse(BranchType.NONE), BranchType::getId);
}