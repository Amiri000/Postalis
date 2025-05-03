package sad.ami.postalis.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import sad.ami.postalis.items.base.interfaces.IPostalis;

public class HeavensForgeBlock extends Block implements IPostalis {
    public HeavensForgeBlock() {
        super(BlockBehaviour.Properties.of());
    }

}
