package sad.ami.postalis.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface ITickableBlockEntity {
    static void tick(Level level, BlockPos pos, BlockState state, HeavensForgeBlockEntity be) {
        be.tick(level, pos, state);
    }

    void tick(Level level, BlockPos pos, BlockState state);
}
