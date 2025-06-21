package sad.ami.postalis.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.entities.DestructiveTornadoEntity;
import sad.ami.postalis.entities.EmbeddedSwordEntity;
import sad.ami.postalis.entities.MagicSealEntity;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Postalis.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<DestructiveTornadoEntity>> DESTRUCTIVE_TORNADO = ENTITIES.register("destructive_tornado",
            () -> EntityType.Builder.<DestructiveTornadoEntity>of((spawnEntityType, level) -> new DestructiveTornadoEntity(level), MobCategory.MISC)
                    .sized(3F, 5F).build("destructive_tornado"));

    public static final DeferredHolder<EntityType<?>, EntityType<EmbeddedSwordEntity>> EMBEDDED_SWORD = ENTITIES.register("embedded_sword",
            () -> EntityType.Builder.<EmbeddedSwordEntity>of((spawnEntityType, level) -> new EmbeddedSwordEntity(level), MobCategory.MISC)
                    .sized(0.4F, 1.2F).build("embedded_sword"));

    public static final DeferredHolder<EntityType<?>, EntityType<MagicSealEntity>> MAGIC_SEAL = ENTITIES.register("magic_seal",
            () -> EntityType.Builder.<MagicSealEntity>of((spawnEntityType, level) -> new MagicSealEntity(level), MobCategory.MISC)
                    .sized(0.5F, 0.5F).build("ornament"));
}
