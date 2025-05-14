package sad.ami.postalis.api.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import sad.ami.postalis.client.interaction.ClientCastAnimation;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class PlayerItemInteractionEvent extends Event implements ICancellableEvent {
    private final Player caster;
    private final Level level;
    private final ClientCastAnimation.UseStage stage;
    private final int tickCount;
}
