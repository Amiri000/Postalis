package sad.ami.postalis.client;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import org.joml.Matrix4f;
import sad.ami.postalis.api.event.RendererItemInHandEvent;
import sad.ami.postalis.client.interaction.ClientCastAnimation;
import sad.ami.postalis.client.screen.ChecklistAbilityScreen;
import sad.ami.postalis.config.PostalisConfig;
import sad.ami.postalis.init.HotkeyRegistry;
import sad.ami.postalis.init.ItemRegistry;
import sad.ami.postalis.items.base.interfaces.IUsageItem;
import sad.ami.postalis.networking.NetworkHandler;
import sad.ami.postalis.networking.packets.sync.S2CTickingUsePacket;
import sad.ami.postalis.utils.PlayerUtils;

@EventBusSubscriber(Dist.CLIENT)
public class ClientPlayerHandlers {
    public static CameraType oldCameraType;
    public static int holdTime = 0;
    private static final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public static void onClientTicking(ClientTickEvent.Post event) {
        var player = mc.player;

        if (player == null)
            return;

        if (Minecraft.getInstance().options.keyUse.isDown() && PlayerUtils.inMainHandPostalisSword(player)) {
            ClientCastAnimation.useTickCount++;
            ClientCastAnimation.putChargeTicks(player, ClientCastAnimation.useTickCount);

            NetworkHandler.sendToServer(new S2CTickingUsePacket(ClientCastAnimation.useTickCount, ClientCastAnimation.UseStage.TICK));
        } else {
            if (ClientCastAnimation.useTickCount != 0) {
                ClientCastAnimation.useTickCount = 0;
                ClientCastAnimation.putChargeTicks(player, ClientCastAnimation.useTickCount);

                NetworkHandler.sendToServer(new S2CTickingUsePacket(ClientCastAnimation.useTickCount, ClientCastAnimation.UseStage.STOP));
            }
        }

        if (!HotkeyRegistry.CHECKLIST_MENU.isDown()) {
            if (holdTime <= 0 || player.tickCount % 2 == 0)
                return;

            holdTime--;
        } else {
            if (PlayerUtils.inMainHandPostalisSword(player)) {
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

    private static Matrix4f savedRot;
    public static Vec3 handPos;
    public static RendererItemInHandEvent ev;

    @SubscribeEvent
    public static void onRenderWorld(RenderLevelStageEvent event) {
        if (mc.level == null || mc.player == null || event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES || savedRot == null || handPos == null)
            return;

        var poseStack = event.getPoseStack();
        var camPos = event.getCamera().getPosition();

        poseStack.pushPose();

        poseStack.translate(handPos.x - camPos.x, handPos.y - camPos.y, handPos.z - camPos.z);
        poseStack.mulPose(savedRot);

        if (mc.options.getCameraType() == CameraType.FIRST_PERSON) {
            poseStack.translate(-0.22F, 0.2F, 0.4F);
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }

        ItemStack stack = new ItemStack(ItemRegistry.WIND_BREAKER.get());

        mc.getItemRenderer().render(stack, ev.getContext(), false, poseStack, ev.getBuffer(), ev.getLight(), OverlayTexture.NO_OVERLAY, mc.getItemRenderer().getModel(stack, mc.level, mc.player, 0));

        poseStack.popPose();
    }

    @SubscribeEvent
    public static void onRenderItem(RendererItemInHandEvent event) {
        var mc = Minecraft.getInstance();

        var stack = event.getStack();
        var context = event.getContext();

        if (mc.player == null || !(stack.getItem() instanceof IUsageItem usageItem) || context == ItemDisplayContext.GUI
                || context == ItemDisplayContext.GROUND || context == ItemDisplayContext.FIXED || context == ItemDisplayContext.HEAD)
            return;

        ev = event;
        Matrix4f rot = new Matrix4f(event.getPoseStack().last().pose());
        rot.setTranslation(0, 0, 0);

        usageItem.onRenderUsage(event.getRenderer(), event.getPlayer(), event.getStack(), event.getContext(), event.getPoseStack(), event.getBuffer(), event.getLight());
        savedRot = rot;
    }

    @SubscribeEvent
    public static void onKeyPress(InputEvent.InteractionKeyMappingTriggered event) {
        if (ClientCastAnimation.useTickCount != 0) {
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
