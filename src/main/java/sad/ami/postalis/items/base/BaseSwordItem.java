package sad.ami.postalis.items.base;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import sad.ami.postalis.init.PDataComponentRegistry;
import sad.ami.postalis.items.base.interfaces.IPostalis;
import sad.ami.postalis.items.base.interfaces.ISwordItem;

import java.util.UUID;

public class BaseSwordItem extends SwordItem implements ISwordItem, IPostalis {
    public BaseSwordItem() {
        super(Tiers.DIAMOND, new Item.Properties().rarity(Rarity.RARE).stacksTo(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (getOwnerUUID(stack) != entity.getUUID())
            setOwnerUUID(stack, entity.getUUID());

        if (!(entity instanceof Player player) || player.getMainHandItem() != stack && player.getOffhandItem() != stack)
            return;

        inMainHand(player, stack, level);
    }

    public void setOwnerUUID(ItemStack stack, UUID uuid) {
        stack.set(PDataComponentRegistry.UUID, uuid);
    }

    public UUID getOwnerUUID(ItemStack stack) {
        return stack.getOrDefault(PDataComponentRegistry.UUID, new UUID(0, 0));
    }
}
