package sad.ami.postalis.handlers;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.api.event.PlayerItemInteractionEvent;
import sad.ami.postalis.api.interaction.ClientCastAnimation;
import sad.ami.postalis.items.base.BaseSwordItem;
import sad.ami.postalis.items.base.interfaces.ISwordItem;
import sad.ami.postalis.items.base.interfaces.IUsageItem;

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
    public static void onInteraction(PlayerItemInteractionEvent event) {
        var player = event.getCaster();

        if (player.getMainHandItem().getItem() instanceof IUsageItem usageItem)
            usageItem.onUsage(event.getCaster(), event.getStage(), event.getLevel(), event.getTickCount());
    }

    @SubscribeEvent
    public static void onPlayerTicking(PlayerTickEvent.Post event) {
        if (event.getEntity().getCommandSenderWorld().isClientSide())
            return;

        var player = event.getEntity();

        if (player.getMainHandItem().getItem() instanceof BaseSwordItem swordItem || ClientCastAnimation.useTickCount == 0)
            return;

        ClientCastAnimation.useTickCount = 0;
    }

    @SubscribeEvent
    public static void onPlayerToss(ItemTossEvent event) {
        var player = event.getPlayer();
        var itemEntity = event.getEntity();

        if (itemEntity.getItem().getItem() instanceof ISwordItem postalisItem)
            postalisItem.onToss(player, itemEntity, player.getCommandSenderWorld());
    }
}