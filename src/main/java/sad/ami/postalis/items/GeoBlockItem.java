package sad.ami.postalis.items;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.api.system.geo.renderer_type.GeoItemRenderer;
import sad.ami.postalis.init.ItemRegistry;

import java.util.function.Consumer;

public class GeoBlockItem extends BlockItem {
    public GeoBlockItem(Block block, Properties properties) {
        super(block, properties);
    }
}
