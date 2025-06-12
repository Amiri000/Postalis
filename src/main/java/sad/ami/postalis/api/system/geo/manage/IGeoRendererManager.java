package sad.ami.postalis.api.system.geo.manage;

import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public interface IGeoRendererManager {
    @OnlyIn(Dist.CLIENT)
    default IClientItemExtensions getCustomRender() {
        return null;
    }

    default Block getBlock() {
        return null;
    }
}
