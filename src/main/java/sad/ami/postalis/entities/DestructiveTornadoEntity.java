package sad.ami.postalis.entities;

import net.minecraft.world.level.Level;
import sad.ami.postalis.entities.base.BaseEntity;
import sad.ami.postalis.init.EntityRegistry;

public class DestructiveTornadoEntity extends BaseEntity {
    public DestructiveTornadoEntity(Level level) {
        super(EntityRegistry.DESTRUCTIVE_TORNADO.get(), level);
    }

}
