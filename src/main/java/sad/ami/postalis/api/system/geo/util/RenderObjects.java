package sad.ami.postalis.api.system.geo.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RenderObjects {
    BLOCK("block"),
    ITEM("item"),
    ENTITY("entity");

    private final String type;
}
