package sad.ami.postalis.client;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import sad.ami.postalis.api.PlayerItemInteraction;
import sad.ami.postalis.client.screen.ChecklistAbilityScreen;
import sad.ami.postalis.config.PostalisConfig;
import sad.ami.postalis.handlers.PlayerEventHandlers;
import sad.ami.postalis.init.HotkeyRegistry;
import sad.ami.postalis.init.ItemRegistry;
import sad.ami.postalis.utils.PlayerUtils;

@EventBusSubscriber(Dist.CLIENT)
public class ClientPlayerHandlers {
    public static CameraType oldCameraType;
    public static int holdTime = 0;
    private static final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!event.getEntity().getCommandSenderWorld().isClientSide() || !(event.getEntity() instanceof LocalPlayer localPlayer))
            return;

        if (!HotkeyRegistry.CHECKLIST_MENU.isDown()) {
            if (holdTime <= 0 || localPlayer.tickCount % 2 == 0)
                return;

            holdTime--;
        } else {
            if (PlayerUtils.inMainHandPostalisSword(localPlayer)) {
                holdTime++;

                if (holdTime < 20)
                    return;

                mc.setScreen(new ChecklistAbilityScreen());

                oldCameraType = mc.options.getCameraType();

                mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);

                if (PostalisConfig.isHintVisible())
                    PostalisConfig.disabledVisibleHint();
            }
        }
    }

    @SubscribeEvent
    public static void onRenderWorld(RenderLevelStageEvent event) {
        if (mc.level == null || mc.player == null || event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES)
            return;

        var poseStack = event.getPoseStack();
        var itemRenderer = mc.getItemRenderer();
        var camera = event.getCamera();
        var camPos = camera.getPosition();
        var partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);
        var tickCount = PlayerItemInteraction.useTickCount;

        var buffer = mc.renderBuffers().bufferSource();

        for (var player : mc.level.players()) {
            if (player.isInvisible() || !PlayerUtils.inMainHandPostalisSword(player))
                continue;

            double x = Mth.lerp(partialTick, player.xOld, player.getX());
            double y = Mth.lerp(partialTick, player.yOld, player.getY()) + player.getEyeHeight() + 0.6;
            double z = Mth.lerp(partialTick, player.zOld, player.getZ());

            float progress = tickCount == 0 ? 0f : Mth.clamp((tickCount + partialTick) / 20f, 0f, 1f);
            Vec3 lookVec = player.getLookAngle().normalize().scale(progress * 2.0);

            poseStack.pushPose();
            poseStack.translate(x + lookVec.x - camPos.x, y + lookVec.y - camPos.y, z + lookVec.z - camPos.z);
            poseStack.scale(0.5f, 0.5f, 0.5f);

            ItemStack stack = new ItemStack(ItemRegistry.WIND_BREAKER.get());

            itemRenderer.render(stack, ItemDisplayContext.FIXED, player == mc.player, poseStack, buffer, 15728880, OverlayTexture.NO_OVERLAY, itemRenderer.getModel(stack, mc.level, player, 0));

            poseStack.popPose();
        }
    }

    @SubscribeEvent
    public static void onKeyPress(InputEvent.InteractionKeyMappingTriggered event) {
        if (PlayerItemInteraction.useTickCount != 0) {
            event.setSwingHand(false);
            event.setCanceled(true);
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
