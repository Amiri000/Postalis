package sad.ami.postalis.api.interaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerInteractionItem {
    public static int useTickCount = 0;

    @Getter
    @Setter
    private Vec3 itemPosInHand;

    @Getter
    @AllArgsConstructor
    public enum UseStage {
        START("start"),
        TICK("tick"),
        STOP("stop");

        private final String id;

        public static final Map<String, UseStage> BY_ID = Arrays.stream(values()).collect(Collectors.toMap(UseStage::getId, stage -> stage));
    }
}
