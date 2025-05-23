package sad.ami.postalis.init;

import com.mojang.serialization.Codec;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.data.SelectedBranchData;
import sad.ami.postalis.items.base.interfaces.IBranchableItem;

import java.util.UUID;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class PDataComponentRegistry {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Postalis.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SelectedBranchData>> SELECTED_BRANCH = DATA_COMPONENTS.register("selected_branch",
            () -> DataComponentType.<SelectedBranchData>builder().persistent(SelectedBranchData.CODEC).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> STRING = DATA_COMPONENTS.register("string",
            () -> DataComponentType.<String>builder().persistent(Codec.STRING).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> UUID = DATA_COMPONENTS.register("uuid",
            () -> DataComponentType.<java.util.UUID>builder().persistent(UUIDUtil.CODEC).build());

    @SubscribeEvent
    public static void setDataComponents(ModifyDefaultComponentsEvent event) {
        event.modifyMatching(item -> item instanceof IBranchableItem, builder -> builder.set(SELECTED_BRANCH.get(), new SelectedBranchData()));
    }
}
