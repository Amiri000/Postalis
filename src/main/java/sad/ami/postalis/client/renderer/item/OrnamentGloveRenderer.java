package sad.ami.postalis.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import sad.ami.postalis.api.system.geo.GeoRenderer;
import sad.ami.postalis.api.system.geo.manage.GeoModelManager;
import sad.ami.postalis.api.system.geo.samples.ResourceAssetsSample;
import sad.ami.postalis.api.system.geo.util.ItemEntityRenderer;

public class OrnamentGloveRenderer extends ItemEntityRenderer {
    private final ResourceLocation model;
    private final ResourceLocation texture;

    public OrnamentGloveRenderer(ResourceAssetsSample sample) {
        this.model = sample.getModel();
        this.texture = sample.getTexture();
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack pose, MultiBufferSource buf, int light, int overlay) {
        var geo = GeoModelManager.get(model);

        if (context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || context == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
            return;

        pose.pushPose();

        if (context == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) {
            var modifier = 1f / 29f;

            pose.translate(0.65, 0.1, 0.3);
            pose.scale(modifier, modifier, modifier);
            pose.mulPose(Axis.XP.rotationDegrees(5));
            pose.mulPose(Axis.YP.rotationDegrees(-132));
        }

        GeoRenderer.INSTANCE.drawModel(pose, buf, texture, geo, context, overlay, light);

        pose.popPose();
    }
}
