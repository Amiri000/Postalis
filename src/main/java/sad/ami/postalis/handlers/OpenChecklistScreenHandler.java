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
import sad.ami.postalis.utils.PlayerUtils;

@EventBusSubscriber(Dist.CLIENT)
public class OpenChecklistScreenHandler {
    public static CameraType oldCameraType;
    public static int holdTime = 0;

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!event.getEntity().getCommandSenderWorld().isClientSide() || !(event.getEntity() instanceof LocalPlayer localPlayer))
            return;

        if (!HotkeyHandlers.CHECKLIST_MENU.isDown()) {
            if (holdTime <= 0 || localPlayer.tickCount % 2 == 0)
                return;

            holdTime--;
        } else {
            if (PlayerUtils.inMainHandPostalisSword(localPlayer)) {
                holdTime++;

                var mc = Minecraft.getInstance();

                if (holdTime < 20)
                    return;

                mc.setScreen(new ChecklistAbilityScreen());

                oldCameraType = mc.options.getCameraType();

                mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
            }
        }
    }

    @SubscribeEvent
    public static void onRenderGUILayer(RenderGuiLayerEvent.Pre event) {
        if (!PlayerUtils.isChecklistScreen() || !event.getName().toString().equals("minecraft:hotbar"))
            return;

        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        if (!PlayerUtils.isChecklistScreen())
            return;

        event.setCanceled(true);
    }
}
