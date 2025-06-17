package sad.ami.postalis.client.renderer.item;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.api.system.geo.GeoItemEntityRenderer;
import sad.ami.postalis.api.system.geo.GeoRenderer;
import sad.ami.postalis.api.system.geo.manage.GeoModel;
import sad.ami.postalis.api.system.geo.manage.GeoModelManager;
import sad.ami.postalis.api.system.geo.samples.GeoItemRendererBuilder;
import sad.ami.postalis.api.system.geo.samples.ResourceAssetsSample;
import sad.ami.postalis.init.ShaderRegistry;

public class OrnamentGloveRenderer extends GeoItemEntityRenderer {
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

        var modifier = 1f / 29f;

        switch (context) {
            case FIRST_PERSON_RIGHT_HAND -> {
                pose.translate(0.6, 0.1, 0.3);
                pose.scale(modifier, modifier, modifier);
                pose.mulPose(Axis.XP.rotationDegrees(5));
                pose.mulPose(Axis.YP.rotationDegrees(132));
            }
            case FIRST_PERSON_LEFT_HAND -> {
                pose.translate(0.4, 0.1, 0.3);
                pose.scale(modifier, modifier, modifier);
                pose.mulPose(Axis.XP.rotationDegrees(5));
                pose.mulPose(Axis.YP.rotationDegrees(-132));
            }
        }

        var functional = GeoItemRendererBuilder.toBuild()
                .itemDisplayContext(context)
                .modifyGlobalRender(this::modifierGlobalRender)
                .build();

        new GeoRenderer(pose, buf, texture, geo, overlay, light)
                .drawItemModel(functional);

        pose.popPose();
    }

    public void modifierGlobalRender(PoseStack pose, GeoModel.Bone bone) {
        if (bone.name.equals("bone")) {
            pose.translate(0F, 4.5f, 0f);

            if (bone.pivot == null || bone.pivot.size() != 3)
                return;

            Matrix4f boneMatrix = new Matrix4f(pose.last().pose());

            boneMatrix.translate(0, 10.25f, 0);
            boneMatrix.rotateX((float) Math.toRadians(-90));

            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(true);
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableCull();

            RenderSystem.setShader(() -> ShaderRegistry.ORNAMENT_SHADER);
            RenderSystem.setShaderTexture(0, ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "textures/entities/ornament.png"));

            ShaderRegistry.ORNAMENT_SHADER.safeGetUniform("Opacity").set((float) (Math.sin(System.currentTimeMillis() / 300.0) * 0.25 + 0.75));
            ShaderRegistry.ORNAMENT_SHADER.safeGetUniform("Time").set((System.currentTimeMillis() % 100000L) / 1000.0f);

            var consumer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

            float size = 12f;

            consumer.addVertex(boneMatrix, -size, -size, 0).setUv(0, 1);
            consumer.addVertex(boneMatrix, size, -size, 0).setUv(1, 1);
            consumer.addVertex(boneMatrix, size, size, 0).setUv(1, 0);
            consumer.addVertex(boneMatrix, -size, size, 0).setUv(0, 0);

            BufferUploader.drawWithShader(consumer.buildOrThrow());

            RenderSystem.enableCull();
            RenderSystem.disableBlend();
        }
    }
}
