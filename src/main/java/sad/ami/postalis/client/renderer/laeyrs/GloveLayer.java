package sad.ami.postalis.client.renderer.laeyrs;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import org.joml.Matrix4f;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.api.system.geo.GeoRenderer;
import sad.ami.postalis.api.system.geo.manage.GeoModel;
import sad.ami.postalis.api.system.geo.manage.GeoModelManager;
import sad.ami.postalis.api.system.geo.samples.GeoItemRendererBuilder;
import sad.ami.postalis.api.system.geo.samples.ResourceAssetsSample;
import sad.ami.postalis.init.ItemRegistry;
import sad.ami.postalis.init.ShaderRegistry;
import sad.ami.postalis.items.BewitchedGauntletItem;

public class GloveLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public GloveLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buf, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        var rightHandHas = player.getMainHandItem().getItem() instanceof BewitchedGauntletItem;
        var leftHandHas = player.getOffhandItem().getItem() instanceof BewitchedGauntletItem;

        if (!rightHandHas && !leftHandHas)
            return;

        var assets = new ResourceAssetsSample(ItemRegistry.BEWITCHED_GAUNTLET.get());
        var geoModel = GeoModelManager.get(assets.getModel());
        var texture = assets.getTexture();

        var builder = GeoItemRendererBuilder.toBuild()
                .modifyGlobalRender(this::modifierGlobalRender)
                .build();

        float scale = 1f / 29f;

        if (rightHandHas) {
            poseStack.pushPose();

            this.getParentModel().rightArm.translateAndRotate(poseStack);

            poseStack.scale(scale, scale, scale);

            new GeoRenderer(poseStack, buf, texture, geoModel, OverlayTexture.NO_OVERLAY, packedLight)
                    .draw(builder);

            poseStack.popPose();
        }

        if (leftHandHas) {
            poseStack.pushPose();

            this.getParentModel().leftArm.translateAndRotate(poseStack);

            poseStack.scale(-scale, scale, scale);

            new GeoRenderer(poseStack, buf, texture, geoModel, OverlayTexture.NO_OVERLAY, packedLight)
                    .draw(builder);

            poseStack.popPose();
        }
    }

    public void modifierGlobalRender(PoseStack pose, GeoModel.Bone bone) {
        var player = Minecraft.getInstance().player;

        if (player == null)
            return;

        var mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        var offHand = player.getItemInHand(InteractionHand.OFF_HAND);

        var hasValidGauntletInMain = mainHand.getItem() instanceof BewitchedGauntletItem gauntletMain && !gauntletMain.hasSeal(mainHand);
        var hasValidGauntletInOff = offHand.getItem() instanceof BewitchedGauntletItem gauntletOff && !gauntletOff.hasSeal(offHand);

        if ((!hasValidGauntletInMain && !hasValidGauntletInOff) || !bone.name.equalsIgnoreCase("bone"))
            return;

        pose.translate(0F, 4.5f, 0f);

        if (bone.pivot == null || bone.pivot.size() != 3)
            return;

        var boneMatrix = new Matrix4f(pose.last().pose());

        boneMatrix.translate(bone.pivot.get(0), bone.pivot.get(1), bone.pivot.get(2));
        boneMatrix.rotateY((float) Math.toRadians(-90));

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
