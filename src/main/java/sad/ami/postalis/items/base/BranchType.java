package sad.ami.postalis.items.base;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum BranchType {
    NONE("none", Component.translatable("branch_type.postalis.none")),

    WIND("wind", Component.translatable("branch_type.postalis.wind")),
    STORM("storm",  Component.translatable("branch_type.postalis.storm"));

    private final String id;
    private final MutableComponent displayName;

    public static final Codec<BranchType> CODEC = Codec.STRING.flatXmap(id -> Arrays.stream(values())
            .filter(type -> type.id.equals(id))
            .findFirst()
            .map(DataResult::success)
            .orElseGet(() -> DataResult.error(() -> "Unknown BranchType id: " + id)), type -> DataResult.success(type.getId())
    );
}