package sad.ami.postalis.handlers;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import sad.ami.postalis.client.screen.ChecklistAbilityScreen;
import sad.ami.postalis.items.base.ISwordItem;

@EventBusSubscriber(Dist.CLIENT)
public class OpenChecklistScreenHandler {
    public static CameraType oldCameraType;
    public static int holdTime = 0;

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!event.getEntity().getCommandSenderWorld().isClientSide() || !(event.getEntity() instanceof LocalPlayer localPlayer)
                || !(localPlayer.getMainHandItem().getItem() instanceof ISwordItem swordItem))
            return;

        var mc = Minecraft.getInstance();

        if (!HotkeyHandlers.CHECKLIST_MENU.isDown()) {
            if (holdTime == 0 || localPlayer.tickCount % 2 == 0)
                return;

            holdTime--;
        } else {
            holdTime++;

            if (holdTime >= 20) {
                mc.setScreen(new ChecklistAbilityScreen());

                oldCameraType = mc.options.getCameraType();

                mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
            }
        }
    }

    @SubscribeEvent
    public static void onRenderGUILayer(RenderGuiLayerEvent.Pre event) {
        if (!ChecklistAbilityScreen.isChecklistScreen() || !event.getName().toString().equals("minecraft:hotbar"))
            return;

        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        if (!ChecklistAbilityScreen.isChecklistScreen())
            return;

        event.setCanceled(true);
    }
}
