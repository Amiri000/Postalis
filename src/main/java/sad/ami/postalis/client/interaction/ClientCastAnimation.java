package sad.ami.postalis.client.interaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.world.entity.player.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientCastAnimation {
    private static final Map<Integer, Integer> animationTicks = new HashMap<>();
    private static final Map<Integer, Integer> chargeTicksMap = new HashMap<>();
    public static int useTickCount = 0;

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

    @Getter
    @AllArgsConstructor
    public enum UseStage {
        START("start"),
        TICK("tick"),
        STOP("stop");

        private final String id;

        public static final Map<String, ClientCastAnimation.UseStage> BY_ID = Arrays.stream(values()).collect(Collectors.toMap(ClientCastAnimation.UseStage::getId, stage -> stage));
    }
}
