package sad.ami.postalis.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.items.BewitchedGauntletItem;
import sad.ami.postalis.items.WindBreakerItem;
import sad.ami.postalis.items.base.BaseBlockItem;
import sad.ami.postalis.items.base.BaseSwordItem;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Postalis.MODID);

    public static final DeferredHolder<Item, Item> ORNAMENT_GLOVE = ITEMS.register("bewitched_gauntlet", BewitchedGauntletItem::new);

    public static final DeferredHolder<Item, BaseSwordItem> WIND_BREAKER = ITEMS.register("wind_breaker", WindBreakerItem::new);

    public static void register(IEventBus bus) {
        for (var block : BlockRegistry.BLOCKS.getEntries())
            ITEMS.register(block.getId().getPath(), () -> new BaseBlockItem(block.get()));

        ITEMS.register(bus);
    }
}
