package sad.ami.postalis.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.items.base.interfaces.IHoldTickItem;
import sad.ami.postalis.items.base.interfaces.ISwordItem;
import sad.ami.postalis.networking.NetworkHandler;
import sad.ami.postalis.networking.packets.LastTickUsePacket;
import sad.ami.postalis.networking.packets.sync.SyncTickingUsePacket;

@EventBusSubscriber(modid = Postalis.MODID)
public class PlayerEventHandlers {
    public static int holdClientTickCount = 0;

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

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!event.getEntity().getCommandSenderWorld().isClientSide() || !(event.getEntity() instanceof LocalPlayer localPlayer))
            return;

        if (Minecraft.getInstance().options.keyUse.isDown() && localPlayer.getMainHandItem().getItem() instanceof IHoldTickItem) {
            holdClientTickCount++;

            NetworkHandler.sendToServer(new SyncTickingUsePacket(localPlayer.getMainHandItem(), holdClientTickCount, true));
        } else {
            if (holdClientTickCount == 0)
                return;

            holdClientTickCount = 0;

            NetworkHandler.sendToServer(new LastTickUsePacket(holdClientTickCount));
        }
    }
}