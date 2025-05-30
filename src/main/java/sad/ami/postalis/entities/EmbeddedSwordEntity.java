package sad.ami.postalis.entities;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import sad.ami.postalis.entities.base.BaseEntity;
import sad.ami.postalis.init.EntityRegistry;

public class EmbeddedSwordEntity extends BaseEntity {
    public EmbeddedSwordEntity(Level level) {
        super(EntityRegistry.EMBEDDED_SWORD.get(), level);

    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        return InteractionResult.PASS;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return true;
    }


    @Override
    public boolean isInvisible() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
}
