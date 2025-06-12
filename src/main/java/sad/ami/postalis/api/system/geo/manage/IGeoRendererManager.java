package sad.ami.postalis.api.system.geo.manage;

import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public interface IGeoRendererManager {
    default IClientItemExtensions getCustomRender() {
        return null;
    }

    default Block getBlock() {
        return null;
    }
}
