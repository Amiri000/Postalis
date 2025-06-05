package sad.ami.postalis.api.system.geo.renderer_type;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.api.system.geo.GeoModel;
import sad.ami.postalis.api.system.geo.GeoModelManager;
import sad.ami.postalis.api.system.geo.model_data.GeoRenderer;

public abstract class GeoBlockRenderer<T extends BlockEntity> implements BlockEntityRenderer<T>, GeoRenderer {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "geo/test_model.geo.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "textures/block/texture.png");

    @Override
    public final void render(T be, float partialTicks, PoseStack pose, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        GeoModel geo = GeoModelManager.CACHE.get(MODEL);

        if (geo == null || geo.minecraft_geometry.isEmpty())
            return;

        pose.pushPose();

        applyPreTransform(be, partialTicks, pose, bufferSource, packedLight, packedOverlay);

        pose.translate(0.5, 0, 0.5);
        pose.scale(1f / 16f, 1f / 16f, 1f / 16f);

        drawModel(pose, bufferSource.getBuffer(RenderType.entityCutout(TEXTURE)), geo, packedOverlay, packedLight);

        pose.popPose();

        renderExtras(be, partialTicks, pose, bufferSource, packedLight, packedOverlay);
    }

    protected void applyPreTransform(T be, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
    }

    protected void renderExtras(T be, float partialTicks, PoseStack pose, MultiBufferSource buf, int light, int overlay) {
    }
}