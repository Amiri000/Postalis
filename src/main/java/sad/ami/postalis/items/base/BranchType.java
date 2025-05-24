package sad.ami.postalis.items.base;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum BranchType {

    WIND("wind", "Ветер"),
    STORM("storm", "Шторм");

    private final String id;
    private final String displayName;

    public static final Codec<BranchType> CODEC = Codec.STRING.flatXmap(id -> Arrays.stream(values())
            .filter(type -> type.id.equals(id))
            .findFirst()
            .map(DataResult::success)
            .orElseGet(() -> DataResult.error(() -> "Unknown BranchType id: " + id)), type -> DataResult.success(type.getId())
    );
}