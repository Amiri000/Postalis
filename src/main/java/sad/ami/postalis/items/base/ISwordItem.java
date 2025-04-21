package sad.ami.postalis.items.base;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ISwordItem {
    default void onAttacked(Player player, ItemStack stack, Level level) {
    }

    default void onToss(Player player, ItemEntity itemEntity, Level level){
    }

    default void inHand(Player player, ItemStack stack, Level level){
    }
}
