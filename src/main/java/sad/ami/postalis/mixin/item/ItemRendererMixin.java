package sad.ami.postalis.mixin.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.init.PDataComponentRegistry;
import sad.ami.postalis.items.base.BaseSwordItem;
import sad.ami.postalis.networking.NetworkHandler;
import sad.ami.postalis.networking.packets.sync.SyncPosItemInHandPacket;

@Mixin(ItemInHandRenderer.class)
public class ItemRendererMixin {
    @Inject(method = "renderItem", at = @At("HEAD"))
    private void onRenderItem(LivingEntity entity, ItemStack stack, ItemDisplayContext context, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int seed, CallbackInfo ci) {
        Postalis.fgfg(stack, context, leftHand, poseStack, buffer, ci);
    }

    @Inject(method = "renderItem", at = @At("RETURN"))
    private void onRenderItemPost(LivingEntity entity, ItemStack stack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int seed, CallbackInfo ci) {
        if (!(stack.getItem() instanceof BaseSwordItem baseSword))
            return;

        var mc = Minecraft.getInstance();
        var player = mc.player;

        if (mc.isPaused() || player == null || player.tickCount % 5 != 0)
            return;

        var localPos = new Vector4f(0, 0, 0, 1).mul(poseStack.last().pose());
        var camPos = mc.gameRenderer.getMainCamera().getPosition();

        NetworkHandler.sendToServer(new SyncPosItemInHandPacket(new Vec3(camPos.x + localPos.x(), camPos.y + localPos.y(), camPos.z + localPos.z()), stack.get(PDataComponentRegistry.UUID), stack));
    }
}
