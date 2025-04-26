package sad.ami.postalis.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import sad.ami.postalis.client.screen.ChecklistAbilityScreen;
import sad.ami.postalis.items.base.ISwordItem;

public class PlayerUtils {
    public static boolean inMainHandPostalisSword(Player player) {
        return player.getMainHandItem().getItem() instanceof ISwordItem;
    }

    public static boolean isChecklistScreen() {
        var mc = Minecraft.getInstance();

        if (mc.player == null)
            return false;

        return mc.player.getMainHandItem().getItem() instanceof ISwordItem && mc.screen instanceof ChecklistAbilityScreen;
    }
}
