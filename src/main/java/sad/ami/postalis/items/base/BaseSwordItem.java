package sad.ami.postalis.items.base;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class BaseSwordItem extends SwordItem implements ISwordItem, IPostalis {
    public BaseSwordItem() {
        super(Tiers.DIAMOND, new Item.Properties().rarity(Rarity.RARE).stacksTo(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (!(entity instanceof Player player) || player.getMainHandItem() != stack && player.getOffhandItem() != stack)
            return;

        inHand(player, stack, level);
    }
}
