package sad.ami.postalis.mixin.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
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

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Inject(method = "render", at = @At("HEAD"))
    private void onRenderItem(ItemStack stack, ItemDisplayContext context, boolean leftHanded, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, BakedModel model, CallbackInfo ci) {
        Postalis.fgfg(stack, context, leftHanded, poseStack, buffer, light, overlay, model, ci);
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void onRenderItemPost(ItemStack stack, ItemDisplayContext context, boolean leftHanded, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, BakedModel model, CallbackInfo ci) {
        if (!(stack.getItem() instanceof BaseSwordItem baseSword))
            return;

        var mc = Minecraft.getInstance();
        var player = mc.player;

        if (mc.isPaused() || player == null || player.tickCount % 5 != 0 || context != ItemDisplayContext.FIRST_PERSON_RIGHT_HAND && context != ItemDisplayContext.FIRST_PERSON_LEFT_HAND
                && context != ItemDisplayContext.THIRD_PERSON_RIGHT_HAND && context != ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
            return;

        var localPos = new Vector4f(0, 0, 0, 1).mul(poseStack.last().pose());
        var camPos = mc.gameRenderer.getMainCamera().getPosition();

        NetworkHandler.sendToServer(new SyncPosItemInHandPacket(new Vec3(camPos.x + localPos.x(), camPos.y + localPos.y(), camPos.z + localPos.z()), stack.get(PDataComponentRegistry.UUID), stack));
    }
}
