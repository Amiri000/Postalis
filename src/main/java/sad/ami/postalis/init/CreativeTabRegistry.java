package sad.ami.postalis.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.items.base.IPostalis;

public class CreativeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Postalis.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> POSTALIS_TAB = CREATIVE_TAB.register("postalis", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("creative.tab.postalis"))
                    .icon(() -> ItemRegistry.WIND_BREAKER.get().getDefaultInstance())
                    .displayItems((parameters, output) -> BuiltInRegistries.ITEM.stream().filter(i -> i instanceof IPostalis
                                    || i instanceof BlockItem blockItem && blockItem.getBlock() instanceof IPostalis)
                            .map(ItemStack::new).forEach(output::accept))
                    .build()
    );

}
