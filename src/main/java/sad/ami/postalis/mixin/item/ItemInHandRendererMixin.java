package sad.ami.postalis.mixin.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.api.event.RendererItemInHandEvent;
import sad.ami.postalis.client.ClientPlayerHandlers;
import sad.ami.postalis.init.ItemRegistry;
import sad.ami.postalis.items.base.BaseSwordItem;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
    @Shadow
    @Final
    private ItemRenderer itemRenderer;

    @Inject(method = "renderItem", at = @At("HEAD"))
    private void onRenderItem(LivingEntity entity, ItemStack stack, ItemDisplayContext context, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int seed, CallbackInfo ci) {
        if (!(stack.getItem() instanceof BaseSwordItem baseSword) || !(entity instanceof Player player))
            return;

        var event = NeoForge.EVENT_BUS.post(new RendererItemInHandEvent(itemRenderer, player, stack, context, poseStack, buffer, seed));

        if (Minecraft.getInstance().isPaused() || event.isCanceled())
            return;

        event.getRenderer().renderStatic(event.getPlayer(), event.getStack(), event.getContext(), leftHand, event.getPoseStack(), event.getBuffer(), event.getPlayer().level(), event.getLight(), seed, OverlayTexture.NO_OVERLAY);
    }

    @Inject(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", ordinal = 0, shift = At.Shift.AFTER))
    private void postalis$afterIsEmptyCheck(AbstractClientPlayer player, float partialTicks, float pitch, InteractionHand hand, float swingProgress, ItemStack stack, float equippedProgress, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, CallbackInfo ci) {
        Postalis.sssa(player, partialTicks, pitch, hand, swingProgress, stack, equippedProgress, poseStack, buffer, combinedLight, ci);
    }

    @Inject(method = "renderItem", at = @At("RETURN"))
    private void onRenderItemPost(LivingEntity entity, ItemStack stack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int seed, CallbackInfo ci) {
        if (!(stack.getItem() instanceof BaseSwordItem baseSword))
            return;

        var mc = Minecraft.getInstance();
        var player = mc.player;

        if (mc.isPaused() || player == null)
            return;

        var localPos = new Vector4f(0, 0, 0, 1).mul(poseStack.last().pose());
        var camPos = mc.gameRenderer.getMainCamera().getPosition();

        ClientPlayerHandlers.handPos = new Vec3(camPos.x + localPos.x(), camPos.y + localPos.y(), camPos.z + localPos.z());
    }

}
