package sad.ami.postalis.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import sad.ami.postalis.entities.base.BaseEntity;
import sad.ami.postalis.init.EntityRegistry;

public class OrnamentEntity extends BaseEntity {
    public OrnamentEntity(Level level) {
        super(EntityRegistry.ORNAMENT.get(), level);
    }
}
