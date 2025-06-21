package sad.ami.postalis.client;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import org.joml.Matrix4f;
import sad.ami.postalis.api.event.RendererItemInHandEvent;
import sad.ami.postalis.client.interaction.ClientCastAnimation;
import sad.ami.postalis.client.screen.ChecklistAbilityScreen;
import sad.ami.postalis.client.screen.UpgradeAbilityScreen;
import sad.ami.postalis.config.PostalisConfig;
import sad.ami.postalis.init.HotkeyRegistry;
import sad.ami.postalis.items.base.interfaces.IBranchableItem;
import sad.ami.postalis.networking.NetworkHandler;
import sad.ami.postalis.networking.packets.sync.C2SBeginCastPacket;
import sad.ami.postalis.utils.PlayerUtils;

@EventBusSubscriber(Dist.CLIENT)
public class ClientPlayerHandlers {
    public static int holdTime = 0;
    public static CameraType oldCameraType;
    private static BlockPos startCastPos;
    private static final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public static void onClientTicking(ClientTickEvent.Post event) {
        var player = mc.player;

        if (player == null)
            return;

        var chargeTicks = ClientCastAnimation.getChargeTicks(player);

        if (Minecraft.getInstance().options.keyUse.isDown() && PlayerUtils.inMainHandPostalisSword(player)) {
            if (!(player.pick(player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE), 0.0F, false) instanceof BlockHitResult blockHitResult))
                return;

            var pos = blockHitResult.getBlockPos();
            var level = player.getCommandSenderWorld();

            if (chargeTicks == 0)
                startCastPos = pos;

            var state = level.getBlockState(pos);
            var shape = state.getCollisionShape(level, pos);

            if (shape.isEmpty() || (startCastPos != null && !startCastPos.equals(pos))) {
                ClientCastAnimation.consumeChargeTick(player, 3);

                return;
            }

            var aabb = shape.bounds();

            if (state.isAir() || Math.max(aabb.maxX - aabb.minX, aabb.maxZ - aabb.minZ) < 0.5F)
                return;

            ClientCastAnimation.addChargeTick(player);

            if (chargeTicks >= 90) {
                NetworkHandler.sendToServer(new C2SBeginCastPacket(chargeTicks, pos));

                ClientCastAnimation.putChargeTicks(player, 0);
                ClientCastAnimation.remove(player);
            }
        }

        if (chargeTicks != 0 && (!Minecraft.getInstance().options.keyUse.isDown() || !PlayerUtils.inMainHandPostalisSword(player))) {
            ClientCastAnimation.consumeChargeTick(player, 3);

            startCastPos = null;
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

    @SubscribeEvent
    public static void onKeyPress(InputEvent.InteractionKeyMappingTriggered event) {
        if (Minecraft.getInstance().player != null && ClientCastAnimation.getChargeTicks(Minecraft.getInstance().player) != 0) {
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

    public static void openAbilityScreen(BlockPos blockPos) {
        Minecraft.getInstance().setScreen(new UpgradeAbilityScreen(blockPos));
    }
}
