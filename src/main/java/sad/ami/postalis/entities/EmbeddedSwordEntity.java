package sad.ami.postalis.entities;

import net.minecraft.world.level.Level;
import sad.ami.postalis.entities.base.BaseEntity;
import sad.ami.postalis.init.EntityRegistry;

public class EmbeddedSwordEntity extends BaseEntity {
    public EmbeddedSwordEntity(Level level) {
        super(EntityRegistry.EMBEDDED_SWORD.get(), level);
    }
}
