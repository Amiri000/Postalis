package sad.ami.postalis.client.renderer.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import sad.ami.postalis.init.ItemRegistry;

public class EmbeddedSwordRenderer<T extends Entity> extends EntityRenderer<T> {
    public EmbeddedSwordRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(T p_entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(p_entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        poseStack.pushPose();

        poseStack.mulPose(Axis.XP.rotationDegrees(180f));
        poseStack.translate(0.0, -0.5, 0.0);

        Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(ItemRegistry.WIND_BREAKER), ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, null, 0);

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TextureAtlas.LOCATION_PARTICLES;
    }
}
