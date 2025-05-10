package sad.ami.postalis.client.interaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientCastAnimation {
    public static final Map<Integer, Vec3> activeRenders = new HashMap<>();
    private static final Map<Integer, Integer> chargeTicksMap = new HashMap<>();

    public static int useTickCount = 0;

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
