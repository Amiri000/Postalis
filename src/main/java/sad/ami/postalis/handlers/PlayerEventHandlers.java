package sad.ami.postalis.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.SwordItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.client.screen.ChecklistAbilityScreen;
import sad.ami.postalis.client.screen.base.IPostalisScreen;
import sad.ami.postalis.items.base.ISwordItem;

@EventBusSubscriber(modid = Postalis.MODID)
public class PlayerEventHandlers {
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!event.getEntity().getCommandSenderWorld().isClientSide() || !(event.getEntity() instanceof LocalPlayer localPlayer)
                || !(localPlayer.getMainHandItem().getItem() instanceof ISwordItem swordItem) || !HotkeyHandlers.CHECKLIST_MENU.isDown())
            return;

        Minecraft mc = Minecraft.getInstance();

//        if (mc.options.getCameraType() != CameraType.THIRD_PERSON_BACK)
//            mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
        mc.setScreen(new ChecklistAbilityScreen());
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null || !(mc.player.getMainHandItem().getItem() instanceof ISwordItem) || !(Minecraft.getInstance().screen instanceof IPostalisScreen))
            return;

        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onPlayerAttacked(AttackEntityEvent event) {
        var player = event.getEntity();
        var stack = player.getMainHandItem();

        if (stack.getItem() instanceof ISwordItem postalisItem)
            postalisItem.onAttacked(player, stack, player.getCommandSenderWorld());
    }

    @SubscribeEvent
    public static void onPlayerToss(ItemTossEvent event) {
        var player = event.getPlayer();
        var itemEntity = event.getEntity();

        if (itemEntity.getItem().getItem() instanceof ISwordItem postalisItem)
            postalisItem.onToss(player, itemEntity, player.getCommandSenderWorld());
    }
}