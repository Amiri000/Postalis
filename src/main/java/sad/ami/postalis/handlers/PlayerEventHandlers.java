package sad.ami.postalis.handlers;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.api.event.PlayerItemInteractionEvent;
import sad.ami.postalis.api.interaction.PlayerInteractionItem;
import sad.ami.postalis.items.base.BaseSwordItem;
import sad.ami.postalis.items.base.interfaces.ISwordItem;
import sad.ami.postalis.networking.NetworkHandler;
import sad.ami.postalis.networking.packets.sync.animations.BroadcastChargeTicksPacket;

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
        var caster = event.getCaster();
//        for (var target : event.getLevel().players())
//            NetworkHandler.sendToClient(new BroadcastChargeTicksPacket(event.getCaster().getId(), event.getTickCount()), (ServerPlayer) target);

        if (caster.getCommandSenderWorld() instanceof ServerLevel serverLevel)
            for (ServerPlayer player : serverLevel.getChunkSource().chunkMap.getPlayers(caster.chunkPosition(), false))
                NetworkHandler.sendToClient(new BroadcastChargeTicksPacket(caster.getId(),  event.getTickCount()), player);
    }

    @SubscribeEvent
    public static void onPlayerTicking(PlayerTickEvent.Post event) {
        if (event.getEntity().getCommandSenderWorld().isClientSide())
            return;

        var player = event.getEntity();

        if (player.getMainHandItem().getItem() instanceof BaseSwordItem swordItem || PlayerInteractionItem.useTickCount == 0)
            return;

        PlayerInteractionItem.useTickCount = 0;
        //  NeoForge.EVENT_BUS.post(new PlayerItemInteractionEvent(player, player.getCommandSenderWorld(), PlayerInteractionItem.UseStage.STOP, PlayerInteractionItem.useTickCount));

    }

    @SubscribeEvent
    public static void onPlayerToss(ItemTossEvent event) {
        var player = event.getPlayer();
        var itemEntity = event.getEntity();

        if (itemEntity.getItem().getItem() instanceof ISwordItem postalisItem)
            postalisItem.onToss(player, itemEntity, player.getCommandSenderWorld());
    }
}