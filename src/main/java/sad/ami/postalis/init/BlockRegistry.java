package sad.ami.postalis.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.block.HeavensForgeBlock;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, Postalis.MODID);

    public static final DeferredHolder<Block, HeavensForgeBlock> HEAVENS_FORGE = BLOCKS.register("heavens_forge", HeavensForgeBlock::new);

}
