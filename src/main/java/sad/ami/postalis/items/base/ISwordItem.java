package sad.ami.postalis.items.base;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ISwordItem {
    default void onAttacked(Player player, LivingEntity target, ItemStack stack, Level level) {
    }

    default void onToss(Player player, ItemEntity itemEntity, Level level){
    }

    default void inMainHand(Player player, ItemStack stack, Level level){
    }
}
