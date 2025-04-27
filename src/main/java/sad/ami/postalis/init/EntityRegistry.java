package sad.ami.postalis.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.entities.DestructiveTornadoEntity;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Postalis.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<DestructiveTornadoEntity>> DESTRUCTIVE_TORNADO = ENTITIES.register("destructive_tornado",
            () -> EntityType.Builder.of(DestructiveTornadoEntity::new, MobCategory.MISC).sized(3F, 5F).build("destructive_tornado"));

}
