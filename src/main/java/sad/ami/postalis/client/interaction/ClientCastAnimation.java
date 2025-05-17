package sad.ami.postalis.client.interaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import sad.ami.postalis.networking.NetworkHandler;
import sad.ami.postalis.networking.packets.sync.C2STickingUsePacket;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientCastAnimation {
    public static final Map<Integer, Vec3> activeRenders = new HashMap<>();
    private static final Map<Integer, Integer> chargeTicks = new HashMap<>();

    public static void consumeChargeTick(Player player, int val) {
        putChargeTicks(player, getChargeTicks(player) - val);

        NetworkHandler.sendToServer(new C2STickingUsePacket(player.getId(), getChargeTicks(player) - val));
    }

    public static void addChargeTick(Player player) {
        putChargeTicks(player, getChargeTicks(player) + 1);

        NetworkHandler.sendToServer(new C2STickingUsePacket(player.getId(), getChargeTicks(player) + 1));
    }

    public static void remove(Player player) {
        chargeTicks.remove(player.getId());
    }

    public static void putChargeTicks(Player player, int ticks) {
        putChargeTicks(player.getId(), ticks);
    }

    public static void putChargeTicks(int id, int ticks) {
        chargeTicks.put(id, Math.max(0, ticks));
    }

    public static int getChargeTicks(Player player) {
        return chargeTicks.getOrDefault(player.getId(), 0);
    }

    @Getter
    @AllArgsConstructor
    public enum UseStage {
        COMPLETED("completed"),
        TICK("tick"),
        STOP("stop");

        private final String id;

        public static final Map<String, ClientCastAnimation.UseStage> BY_ID = Arrays.stream(values()).collect(Collectors.toMap(ClientCastAnimation.UseStage::getId, stage -> stage));
    }
}
