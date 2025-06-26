package sad.ami.postalis.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import sad.ami.postalis.api.system.geo.GeoItemEntityRenderer;
import sad.ami.postalis.api.system.geo.GeoRenderer;
import sad.ami.postalis.api.system.geo.manage.GeoModel;
import sad.ami.postalis.api.system.geo.manage.GeoModelManager;
import sad.ami.postalis.api.system.geo.samples.GeoItemRendererBuilder;
import sad.ami.postalis.api.system.geo.samples.ResourceAssetsSample;
import sad.ami.postalis.init.PostalisRenderTypes;
import sad.ami.postalis.init.ShaderRegistry;
import sad.ami.postalis.items.BewitchedGauntletItem;

public class BewitchedGauntletRenderer extends GeoItemEntityRenderer {
    private final ResourceLocation model;
    private final ResourceLocation texture;

    public BewitchedGauntletRenderer(ResourceAssetsSample sample) {
        this.model = sample.getModel();
        this.texture = sample.getTexture();
    }

    @Override
    public void renderItem(ItemStack stack, ItemDisplayContext context, PoseStack pose, MultiBufferSource buf, int light, int overlay) {
        var geo = GeoModelManager.get(model);

        if (context != ItemDisplayContext.GROUND)
            return;

        pose.pushPose();

        var modifier = 2;

        pose.scale(modifier, modifier, modifier);

        var functional = GeoItemRendererBuilder.toBuild()
                .modifyGlobalRender(BewitchedGauntletRenderer::modifierGlobalRender)
                .build();

        new GeoRenderer(pose, buf, texture, geo, overlay, light)
                .draw(functional);

        pose.popPose();
    }

    public static void modifierGlobalRender(PoseStack pose, GeoModel.Bone bone) {
        var player = Minecraft.getInstance().player;

        if (player == null)
            return;

        var mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        var offHand = player.getItemInHand(InteractionHand.OFF_HAND);

        var hasValidGauntletInMain = mainHand.getItem() instanceof BewitchedGauntletItem gauntletMain && !gauntletMain.hasSeal(mainHand);
        var hasValidGauntletInOff = offHand.getItem() instanceof BewitchedGauntletItem gauntletOff && !gauntletOff.hasSeal(offHand);

        if ((!hasValidGauntletInMain && !hasValidGauntletInOff) || !bone.name.equalsIgnoreCase("seal") || bone.pivot == null || bone.pivot.size() != 3)
            return;

        var boneMatrix = new Matrix4f(pose.last().pose());

        boneMatrix.translate(bone.pivot.get(0), bone.pivot.get(1), bone.pivot.get(2));
        boneMatrix.rotateY((float) Math.toRadians(-90));

        ShaderRegistry.ORNAMENT_SHADER.safeGetUniform("Opacity").set((float) (Math.sin(System.currentTimeMillis() / 300.0) * 0.25 + 0.75));
        ShaderRegistry.ORNAMENT_SHADER.safeGetUniform("Time").set((System.currentTimeMillis() % 100000L) / 1000.0f);

        var size = 6;
        var consumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(PostalisRenderTypes.SEAL_RENDER_TYPE);

        consumer.addVertex(boneMatrix, -size, -size, 0).setUv(0, 1);
        consumer.addVertex(boneMatrix, size, -size, 0).setUv(1, 1);
        consumer.addVertex(boneMatrix, size, size, 0).setUv(1, 0);
        consumer.addVertex(boneMatrix, -size, size, 0).setUv(0, 0);
    }
}
