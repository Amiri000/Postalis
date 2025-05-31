package sad.ami.postalis.client.renderer.entities;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.init.ShaderRegistry;

public class OrnamentRenderer<T extends Entity> extends EntityRenderer<T> {
    public OrnamentRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        RenderSystem.enableBlend();

        poseStack.pushPose();

        poseStack.translate(0, 0.25, 0);
        poseStack.scale(2f, 2f, 2f);

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

        var size = 0.5f;
        var mat = poseStack.last().pose();

        consumer.addVertex(mat, -size, -size, 0).setUv(0, 1);
        consumer.addVertex(mat, size, -size, 0).setUv(1, 1);
        consumer.addVertex(mat, size, size, 0).setUv(1, 0);
        consumer.addVertex(mat, -size, size, 0).setUv(0, 0);

        poseStack.popPose();

        BufferUploader.drawWithShader(consumer.buildOrThrow());

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "textures/entities/ornament.png");
    }
}
