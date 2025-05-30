package sad.ami.postalis.client.renderer.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import sad.ami.postalis.Postalis;

public class OrnamentRenderer<T extends Entity> extends EntityRenderer<T> {
    public OrnamentRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        poseStack.pushPose();

        poseStack.translate(0, 0.25, 0);
        poseStack.scale(2f, 2f, 2f);

        var texture = ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "textures/entities/ornament.png");
        var renderType = RenderType.entityTranslucent(texture);
        var consumer = bufferSource.getBuffer(renderType);

        var pose = poseStack.last();
        float size = 0.5f;

        var overlayU = OverlayTexture.NO_OVERLAY >> 16;
        var overlayV = OverlayTexture.NO_OVERLAY & 0xFFFF;
        var lightU = packedLight >> 16;
        var lightV = packedLight & 0xFFFF;

        consumer.addVertex(pose.pose(), -size, -size, 0).setColor(255, 255, 255, 255)
                .setUv(0, 1).setUv1(overlayU, overlayV).setUv2(lightU, lightV).setNormal(0, 1, 0);

        consumer.addVertex(pose.pose(),  size, -size, 0).setColor(255, 255, 255, 255)
                .setUv(1, 1).setUv1(overlayU, overlayV).setUv2(lightU, lightV).setNormal(0, 1, 0);

        consumer.addVertex(pose.pose(),  size,  size, 0).setColor(255, 255, 255, 255)
                .setUv(1, 0).setUv1(overlayU, overlayV).setUv2(lightU, lightV).setNormal(0, 1, 0);

        consumer.addVertex(pose.pose(), -size,  size, 0).setColor(255, 255, 255, 255)
                .setUv(0, 0).setUv1(overlayU, overlayV).setUv2(lightU, lightV).setNormal(0, 1, 0);

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "textures/entities/ornament.png");
    }
}
