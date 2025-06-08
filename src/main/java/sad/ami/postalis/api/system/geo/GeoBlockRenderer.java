package sad.ami.postalis.api.system.geo;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import sad.ami.postalis.api.system.geo.manage.GeoModel;
import sad.ami.postalis.api.system.geo.manage.IGeoRendererManager;
import sad.ami.postalis.api.system.geo.util.RenderObjects;

public class GeoBlockRenderer<T extends BlockEntity> implements BlockEntityRenderer<T>, IGeoRendererManager {
    private final GeoModel geo;
    private final ResourceLocation texture;

    public GeoBlockRenderer(Block block) {
        var name = BuiltInRegistries.BLOCK.getKey(block).getPath();

        this.geo = getGeoModel(RenderObjects.BLOCK, name);
        this.texture = getTexturePath(RenderObjects.BLOCK, name);
    }

    @Override
    public final void render(T be, float partialTicks, PoseStack pose, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (geo == null || geo.minecraft_geometry.isEmpty())
            return;

        pose.pushPose();

        applyPreTransform(be, partialTicks, pose, bufferSource, packedLight, packedOverlay);

        pose.translate(0.5, 0, 0.5);
        pose.scale(1f / 16f, 1f / 16f, 1f / 16f);

        drawModel(pose, bufferSource.getBuffer(RenderType.entityCutout(texture)), geo, packedOverlay, packedLight);

        pose.popPose();

        renderExtras(be, partialTicks, pose, bufferSource, packedLight, packedOverlay);
    }

    protected void applyPreTransform(T be, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
    }

    protected void renderExtras(T be, float partialTicks, PoseStack pose, MultiBufferSource buf, int light, int overlay) {
    }
}