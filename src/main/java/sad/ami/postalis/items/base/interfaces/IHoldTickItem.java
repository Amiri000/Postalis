package sad.ami.postalis.items.base.interfaces;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IHoldTickItem {
    default void onHeldTickInMainHand(Player player, Level level, int tickCount) {

    }

    default void onStoppedUssd(Player player, ItemStack stack, Level level) {

    }
}
