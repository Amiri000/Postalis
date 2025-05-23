package sad.ami.postalis.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sad.ami.postalis.Postalis;

public class CreativeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Postalis.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> POSTALIS_TAB = CREATIVE_TAB.register(Postalis.MODID, () -> CreativeModeTab.builder()
            .title(Component.translatable("creative.postalis.tab")).icon(() -> ItemRegistry.WIND_BREAKER.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                        for (Item item : BuiltInRegistries.ITEM)
                            if (Postalis.MODID.equals(BuiltInRegistries.ITEM.getKey(item).getNamespace()))
                                output.accept(new ItemStack(item));
                    }
            ).build());
}
