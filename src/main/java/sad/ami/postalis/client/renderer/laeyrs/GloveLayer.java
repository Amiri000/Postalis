package sad.ami.postalis.client.renderer.laeyrs;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import sad.ami.postalis.api.system.geo.GeoRenderer;
import sad.ami.postalis.api.system.geo.manage.GeoModelManager;
import sad.ami.postalis.api.system.geo.samples.ResourceAssetsSample;
import sad.ami.postalis.init.ItemRegistry;
import sad.ami.postalis.items.OrnamentGlove;

public class GloveLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public GloveLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!(player.getMainHandItem().getItem() instanceof OrnamentGlove))
            return;

        PlayerModel<AbstractClientPlayer> model = this.getParentModel();
        ModelPart hand = model.rightArm;

        poseStack.pushPose();

        hand.translateAndRotate(poseStack);

        var modifier = 1f / 29f;

        poseStack.scale(modifier, modifier, modifier);

        var assets = new ResourceAssetsSample(ItemRegistry.ORNAMENT_GLOVE.get());
        var geoModel = GeoModelManager.get(assets.getModel());

        GeoRenderer.INSTANCE.drawModel(poseStack, bufferSource, assets.getTexture(), geoModel, OverlayTexture.NO_OVERLAY, packedLight);

        poseStack.popPose();
    }
}
