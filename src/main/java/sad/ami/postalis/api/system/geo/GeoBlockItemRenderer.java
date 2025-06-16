package sad.ami.postalis.api.system.geo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import sad.ami.postalis.api.system.geo.manage.GeoModelManager;
import sad.ami.postalis.api.system.geo.samples.GeoItemRendererBuilder;
import sad.ami.postalis.api.system.geo.samples.ResourceAssetsSample;

public class GeoBlockItemRenderer extends GeoItemEntityRenderer {
    private final ResourceLocation model;
    private final ResourceLocation texture;

    public GeoBlockItemRenderer(ResourceAssetsSample sample) {
        this.model = sample.getModel();
        this.texture = sample.getTexture();
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack pose, MultiBufferSource buf, int light, int overlay) {
        var geo = GeoModelManager.get(model);

        if (geo == null || geo.minecraft_geometry.isEmpty())
            return;

        pose.pushPose();

        var modifier = 0F;

        switch (context) {
            case GUI, FIXED -> {
                modifier = 1f / 26f;

                pose.scale(modifier, modifier, modifier);
                pose.translate(13.5, 5, 0);
                pose.mulPose(Axis.XP.rotationDegrees(25));
                pose.mulPose(Axis.YP.rotationDegrees(45));
            }
            case THIRD_PERSON_LEFT_HAND, THIRD_PERSON_RIGHT_HAND -> {
                modifier = 1f / 40f;

                pose.translate(0.5, 0.6, 0.3);
                pose.scale(modifier, modifier, modifier);
                pose.mulPose(Axis.XP.rotationDegrees(75));
                pose.mulPose(Axis.YP.rotationDegrees(-132));
            }
            case FIRST_PERSON_LEFT_HAND, FIRST_PERSON_RIGHT_HAND -> {
                modifier = 1f / 29f;

                pose.translate(0.65, 0.1, 0.3);
                pose.scale(modifier, modifier, modifier);
                pose.mulPose(Axis.XP.rotationDegrees(5));
                pose.mulPose(Axis.YP.rotationDegrees(-132));
            }
        }

        var functional = GeoItemRendererBuilder.toBuild()
                .itemDisplayContext(context)
                .build();

        new GeoRenderer(pose, buf.getBuffer(RenderType.entityCutout(texture)), geo, overlay, light).drawItemModel(functional);

        pose.popPose();
    }
}
