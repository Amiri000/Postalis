package sad.ami.postalis.handlers;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.items.base.ISwordItem;

@EventBusSubscriber(modid = Postalis.MODID)
public class PlayerEventHandlers {
    @SubscribeEvent
    public static void onPlayerAttacked(AttackEntityEvent event) {
        var player = event.getEntity();
        var stack = player.getMainHandItem();

        if (stack.getItem() instanceof ISwordItem postalisItem && event.getTarget() instanceof LivingEntity target)
            postalisItem.onAttacked(player, target, stack, player.getCommandSenderWorld());
    }

    @SubscribeEvent
    public static void onPlayerToss(ItemTossEvent event) {
        var player = event.getPlayer();
        var itemEntity = event.getEntity();

        if (itemEntity.getItem().getItem() instanceof ISwordItem postalisItem)
            postalisItem.onToss(player, itemEntity, player.getCommandSenderWorld());
    }
}