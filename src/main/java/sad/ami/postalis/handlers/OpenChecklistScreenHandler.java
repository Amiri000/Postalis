package sad.ami.postalis.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import sad.ami.postalis.client.screen.ChecklistAbilityScreen;
import sad.ami.postalis.items.base.ISwordItem;

@EventBusSubscriber(Dist.CLIENT)
public class OpenChecklistScreenHandler {
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
        if (!ChecklistAbilityScreen.isChecklistScreen())
            return;

        event.setCanceled(true);
    }

}
