package sad.ami.postalis;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import sad.ami.postalis.config.PostalisConfig;
import sad.ami.postalis.init.*;

@Mod(Postalis.MODID)
public class Postalis {
    public static final String MODID = "postalis";

    public Postalis(IEventBus modEventBus, ModContainer modContainer) {
        ItemRegistry.register(modEventBus);
        CreativeTabRegistry.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        EntityRegistry.ENTITIES.register(modEventBus);
        BlockEntitiesRegistry.BLOCK_ENTITIES.register(modEventBus);
        PDataComponentRegistry.DATA_COMPONENTS.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, PostalisConfig.CLIENT_SPEC);
    }
}