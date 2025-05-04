package sad.ami.postalis.api;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.phys.Vec3;

public class PlayerItemInteraction {
    public static int useTickCount = 0;

    @Getter
    @Setter
    private Vec3 itemPosInHand;


}
