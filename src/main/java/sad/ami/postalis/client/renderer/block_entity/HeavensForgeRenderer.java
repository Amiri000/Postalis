package sad.ami.postalis.client.renderer.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import sad.ami.postalis.block.block_entity.HeavensForgeBlockEntity;

@OnlyIn(Dist.CLIENT)
public class HeavensForgeRenderer implements BlockEntityRenderer<HeavensForgeBlockEntity> {
    public HeavensForgeRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(HeavensForgeBlockEntity be, float pt, PoseStack pose, MultiBufferSource buf, int light, int overlay) {
        var stack = be.getPedestalItem();

        if (stack.isEmpty())
            return;

        pose.pushPose();

        pose.translate(0.5, 1.1, 0.5);
        pose.mulPose(Axis.XP.rotationDegrees(90));

        pose.mulPose(Axis.ZP.rotationDegrees((Util.getMillis() / 20f) % 360));

        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, 15728880, overlay, pose, buf, null, 0);

        pose.popPose();
    }
}
