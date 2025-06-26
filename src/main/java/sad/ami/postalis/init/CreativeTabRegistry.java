package sad.ami.postalis.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import sad.ami.postalis.Postalis;

public class CreativeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Postalis.MODID);

    public static void register(IEventBus bus) {
        CREATIVE_TAB.register(bus);

        CREATIVE_TAB.register(Postalis.MODID, () -> CreativeModeTab.builder()
                .title(Component.translatable("creative.postalis.tab")).icon(() -> ItemRegistry.BEWITCHED_GAUNTLET.get().getDefaultInstance())
                .displayItems((parameters, output) -> BuiltInRegistries.ITEM.stream()
                        .filter(item -> Postalis.MODID.equals(BuiltInRegistries.ITEM.getKey(item).getNamespace()))
                        .map(ItemStack::new)
                        .forEach(output::accept))
                .build());
    }
}
