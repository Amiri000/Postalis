package sad.ami.postalis.api.system.geo.renderer_type;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.api.system.geo.GeoModel;
import sad.ami.postalis.api.system.geo.GeoModelManager;
import sad.ami.postalis.api.system.geo.model_data.GeoRenderer;

public abstract class GeoBlockRenderer<T extends BlockEntity> implements BlockEntityRenderer<T>, GeoRenderer {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "geo/test_model.geo.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "textures/block/texture.png");

    @Override
    public final void render(T be, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        GeoModel model = GeoModelManager.CACHE.get(MODEL);

        if (model == null || model.minecraft_geometry.isEmpty())
            return;

        var geo = model.minecraft_geometry.getFirst();

        poseStack.pushPose();

        applyPreTransform(be, partialTicks, poseStack, bufferSource, packedLight, packedOverlay);

        poseStack.translate(0.5, 0, 0.5);
        poseStack.scale(1f / 16f, 1f / 16f, 1f / 16f);

        for (var bone : geo.bones)
            for (var cube : bone.cubes)
                drawCube(poseStack, bufferSource.getBuffer(RenderType.entityCutout(TEXTURE)), cube, geo.description.visible_bounds_offset, geo.description.texture_width, geo.description.texture_height, packedOverlay, packedLight);

        poseStack.popPose();

        renderExtras(be, partialTicks, poseStack, bufferSource, packedLight, packedOverlay);
    }

    protected void applyPreTransform(T be, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
    }

    protected void renderExtras(T be, float partialTicks, PoseStack pose, MultiBufferSource buf, int light, int overlay) {
    }
}