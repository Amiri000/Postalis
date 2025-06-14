package sad.ami.postalis.client.renderer.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import sad.ami.postalis.api.system.geo.GeoBlockRenderer;
import sad.ami.postalis.api.system.geo.samples.ResourceAssetsSample;
import sad.ami.postalis.block.block_entity.HeavensForgeBlockEntity;
import sad.ami.postalis.init.BlockRegistry;

    public class HeavensForgeRenderer extends GeoBlockRenderer<HeavensForgeBlockEntity> {
    public HeavensForgeRenderer() {
        super(new ResourceAssetsSample(BlockRegistry.HEAVENS_FORGE.get()));
    }

    @Override
    protected void renderExtras(HeavensForgeBlockEntity be, float partialTicks, PoseStack pose, MultiBufferSource buf, int light, int overlay) {
        var stack = be.getPedestalItem();

        if (stack.isEmpty())
            return;

        pose.pushPose();

        pose.translate(0.5, 1.1, 0.5);
        pose.mulPose(Axis.XP.rotationDegrees(90));
        pose.mulPose(Axis.ZP.rotationDegrees((Util.getMillis() / 20f) % 360));

        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, light, overlay, pose, buf, null, 0);

        pose.popPose();
    }
}
