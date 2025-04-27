package sad.ami.postalis;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import sad.ami.postalis.config.PostalisConfig;
import sad.ami.postalis.init.BlockRegistry;
import sad.ami.postalis.init.CreativeTabRegistry;
import sad.ami.postalis.init.EntityRegistry;
import sad.ami.postalis.init.ItemRegistry;

@Mod(Postalis.MODID)
public class Postalis {
    public static final String MODID = "postalis";

    public Postalis(IEventBus modEventBus, ModContainer modContainer) {
        CreativeTabRegistry.CREATIVE_TAB.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        EntityRegistry.ENTITIES.register(modEventBus);

        ItemRegistry.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, PostalisConfig.CLIENT_SPEC);
    }
}
