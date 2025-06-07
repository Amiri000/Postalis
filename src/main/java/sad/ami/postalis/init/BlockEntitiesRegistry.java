package sad.ami.postalis.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.block.block_entity.HeavensForgeBlockEntity;

public class BlockEntitiesRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Postalis.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<HeavensForgeBlockEntity>> HEAVENS_FORGE = BLOCK_ENTITIES.register("heavens_forge",
            () -> BlockEntityType.Builder.of(HeavensForgeBlockEntity::new, BlockRegistry.HEAVENS_FORGE.get()).build(null));
}
