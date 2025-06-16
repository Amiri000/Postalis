package sad.ami.postalis.api.system.geo;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import sad.ami.postalis.api.system.geo.manage.GeoModel;
import sad.ami.postalis.api.system.geo.samples.ResourceAssetsSample;

public class GeoBlockRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
    private final GeoModel geo;
    private final ResourceLocation texture;

    public GeoBlockRenderer(ResourceAssetsSample sample) {
        this.geo = sample.getGeoModel();
        this.texture = sample.getTexture();
    }

    @Override
    public final void render(T be, float partialTicks, PoseStack pose, MultiBufferSource buf, int packedLight, int packedOverlay) {
        if (geo == null || geo.minecraft_geometry.isEmpty())
            return;

        pose.pushPose();

        applyPreTransform(be, partialTicks, pose, buf, packedLight, packedOverlay);

        pose.translate(0.5, 0, 0.5);
        pose.scale(1f / 16f, 1f / 16f, 1f / 16f);

        new GeoRenderer(pose, buf.getBuffer(RenderType.entityCutout(texture)), geo, packedOverlay, packedLight)
                .draw();

        pose.popPose();

        renderExtras(be, partialTicks, pose, buf, packedLight, packedOverlay);
    }

    protected void applyPreTransform(T be, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
    }

    protected void renderExtras(T be, float partialTicks, PoseStack pose, MultiBufferSource buf, int light, int overlay) {
    }
}