package sad.ami.postalis.items.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BranchType {
    WIND("wind", "Ветер"),
    STORM("storm", "Шторм");

    private final String id;
    private final String displayName;
}