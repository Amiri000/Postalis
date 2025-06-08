package sad.ami.postalis.api.system.geo.util;

import net.minecraft.world.level.block.Block;

public interface IGeoObject {
    default Block getBlock() {
        return null;
    }
}
