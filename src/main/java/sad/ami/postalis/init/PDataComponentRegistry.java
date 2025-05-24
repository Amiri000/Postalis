package sad.ami.postalis.init;

import com.mojang.serialization.Codec;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.items.base.BranchesData;
import sad.ami.postalis.items.base.interfaces.IBranchableItem;

import java.util.UUID;

public class PDataComponentRegistry {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Postalis.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BranchesData>> SELECTED_BRANCH = DATA_COMPONENTS.register("selected_branch",
            () -> DataComponentType.<BranchesData>builder().persistent(BranchesData.CODEC).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> STRING = DATA_COMPONENTS.register("string",
            () -> DataComponentType.<String>builder().persistent(Codec.STRING).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> UUID = DATA_COMPONENTS.register("uuid",
            () -> DataComponentType.<java.util.UUID>builder().persistent(UUIDUtil.CODEC).build());
}
