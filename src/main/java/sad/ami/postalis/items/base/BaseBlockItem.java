package sad.ami.postalis.items.base;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import sad.ami.postalis.api.system.geo.util.IGeoObject;

public class BaseBlockItem extends BlockItem implements IGeoObject {
    private final Block block;

    public BaseBlockItem(Block block) {
        super(block, new Item.Properties().stacksTo(1));

        this.block = block;
    }

    @Override
    public Block getBlock() {
        return block;
    }
}
