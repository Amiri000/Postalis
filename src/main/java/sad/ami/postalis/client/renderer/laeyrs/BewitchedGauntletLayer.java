package sad.ami.postalis.client.renderer.laeyrs;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import sad.ami.postalis.api.system.geo.GeoRenderer;
import sad.ami.postalis.api.system.geo.samples.GeoItemRendererBuilder;
import sad.ami.postalis.api.system.geo.samples.ResourceAssetsSample;
import sad.ami.postalis.client.renderer.item.BewitchedGauntletRenderer;
import sad.ami.postalis.init.ItemRegistry;
import sad.ami.postalis.items.BewitchedGauntletItem;

public class BewitchedGauntletLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public BewitchedGauntletLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buf, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        var rightHandHas = player.getMainHandItem().getItem() instanceof BewitchedGauntletItem;
        var leftHandHas = player.getOffhandItem().getItem() instanceof BewitchedGauntletItem;

        if (!rightHandHas && !leftHandHas)
            return;

        var assets = new ResourceAssetsSample(ItemRegistry.BEWITCHED_GAUNTLET.get());
        var builder = GeoItemRendererBuilder.toBuild()
                .modifyGlobalRender(BewitchedGauntletRenderer::modifierGlobalRender)
                .build();

        float scale = 1f / 17.5f;

        if (rightHandHas) {
            poseStack.pushPose();

            this.getParentModel().rightArm.translateAndRotate(poseStack);
            poseStack.scale(scale, scale, scale);

            poseStack.mulPose(Axis.YP.rotationDegrees(180));
            poseStack.mulPose(Axis.XN.rotationDegrees(181.5F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(3));

            poseStack.translate(0, -12, 0);

            new GeoRenderer(poseStack, buf, assets.getTexture(), assets.getGeoModel(), OverlayTexture.NO_OVERLAY, packedLight)
                    .draw(builder);

            poseStack.popPose();
        }

        if (leftHandHas) {
            poseStack.pushPose();

            this.getParentModel().leftArm.translateAndRotate(poseStack);

            poseStack.scale(-scale, scale, scale);

            poseStack.mulPose(Axis.YP.rotationDegrees(180));
            poseStack.mulPose(Axis.XN.rotationDegrees(181.5F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(3));

            poseStack.translate(0, -12, 0);

            new GeoRenderer(poseStack, buf, assets.getTexture(), assets.getGeoModel(), OverlayTexture.NO_OVERLAY, packedLight)
                    .draw(builder);

            poseStack.popPose();
        }
    }
}
