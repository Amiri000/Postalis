package sad.ami.postalis.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import sad.ami.postalis.client.screen.ChecklistAbilityScreen;
import sad.ami.postalis.init.PDataComponentRegistry;
import sad.ami.postalis.items.base.interfaces.ISwordItem;

import java.util.UUID;

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

    public static boolean isSameUUID(ItemStack stack, UUID uuid) {
        return stack.getOrDefault(PDataComponentRegistry.UUID, new UUID(0, 0)).equals(uuid);
    }
}
