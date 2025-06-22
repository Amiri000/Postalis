package sad.ami.postalis.entities;

import net.minecraft.world.level.Level;
import sad.ami.postalis.entities.base.BaseEntity;
import sad.ami.postalis.init.EntityRegistry;

public class MagicSealEntity extends BaseEntity {
    public MagicSealEntity(Level level) {
        super(EntityRegistry.MAGIC_SEAL.get(), level);
    }

    @Override
    public void tick() {
        if (tickCount >= 2000) {
            discard();
        }
    }


}
