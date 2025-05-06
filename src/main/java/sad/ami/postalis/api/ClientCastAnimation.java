package sad.ami.postalis.api;

import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public class ClientCastAnimation {
    private static final Map<Integer, Integer> animationTicks = new HashMap<>();
    private static final Map<Integer, Integer> chargeTicksMap = new HashMap<>();

    public static void startAnimation(Player player) {
        animationTicks.put(player.getId(), 100);
    }

    public static void stopAnimation(Player player) {
        animationTicks.remove(player.getId());
    }

    public static void clientTick() {
        animationTicks.replaceAll((id, ticks) -> ticks > 0 ? ticks - 1 : 0);
        animationTicks.entrySet().removeIf(entry -> entry.getValue() <= 0);
    }

    public static int getAnimationTicks(Player player) {
        return animationTicks.getOrDefault(player.getId(), 0);
    }

    public static void putChargeTicks(Player player, int ticks) {
        chargeTicksMap.put(player.getId(), ticks);
    }

    public static int getChargeTicks(Player player) {
        return chargeTicksMap.getOrDefault(player.getId(), 0);
    }
}
