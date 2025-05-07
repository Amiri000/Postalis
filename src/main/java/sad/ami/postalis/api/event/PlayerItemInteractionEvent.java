package sad.ami.postalis.api.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;
import sad.ami.postalis.api.interaction.ClientCastAnimation;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class PlayerItemInteractionEvent extends Event {
    private final Player caster;
    private final Level level;
    private final ClientCastAnimation.UseStage stage;
    private final int tickCount;
}
