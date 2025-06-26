package sad.ami.postalis;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sad.ami.postalis.api.system.geo.GeoRenderer;
import sad.ami.postalis.api.system.geo.samples.ResourceAssetsSample;
import sad.ami.postalis.config.PostalisConfig;
import sad.ami.postalis.init.*;

@Mod(Postalis.MODID)
public class Postalis {
    public static final String MODID = "postalis";

    public Postalis(IEventBus modEventBus, ModContainer modContainer) {
        BlockRegistry.BLOCKS.register(modEventBus);
        EntityRegistry.ENTITIES.register(modEventBus);
        CreativeTabRegistry.CREATIVE_TAB.register(modEventBus);
        BlockEntitiesRegistry.BLOCK_ENTITIES.register(modEventBus);
        PDataComponentRegistry.DATA_COMPONENTS.register(modEventBus);

        ItemRegistry.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, PostalisConfig.CLIENT_SPEC);
    }

    public static void ss(AbstractClientPlayer player, float partialTicks, float pitch, InteractionHand hand, float swingProgress, ItemStack stack, float equippedProgress, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, CallbackInfo ci) {
        if (player.getItemInHand(hand).getItem() != ItemRegistry.BEWITCHED_GAUNTLET.get())
            return;

        poseStack.pushPose();

        var arm = (hand == InteractionHand.MAIN_HAND) ? player.getMainArm() : player.getMainArm().getOpposite();
        var isRightArm = arm != HumanoidArm.LEFT;
        var armSide = isRightArm ? 1.0F : -1.0F;

        var swingSqrt = Mth.sqrt(swingProgress);
        var swingSin1 = Mth.sin(swingSqrt * (float) Math.PI);
        var swingSin2 = Mth.sin(swingSqrt * (float) Math.PI * 2);
        var swingSinFull = Mth.sin(swingProgress * (float) Math.PI);
        var swingSinPow = Mth.sin(swingProgress * swingProgress * (float) Math.PI);

        poseStack.translate(armSide * (-0.3F * swingSin1 + 0.64F), -0.6F + equippedProgress * -0.6F + 0.4F * swingSin2, -0.72F + -0.4F * swingSinFull);

        poseStack.mulPose(Axis.YP.rotationDegrees(armSide * 45.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(armSide * swingSin1 * 70.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(armSide * swingSinPow * -20.0F));

        poseStack.translate(armSide * -1.0F, 3.6F, 3.5F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(armSide * 120.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(200.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(armSide * -135.0F));
        poseStack.translate(armSide * 5.6F, 0.0F, 0.0F);

        var renderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player);
        var modifier = 1f / 20f;

        var overlay = LivingEntityRenderer.getOverlayCoords(player, 0.0f);
        var assetsSample = new ResourceAssetsSample(ItemRegistry.BEWITCHED_GAUNTLET.get());

        if (isRightArm) {
            renderer.renderRightHand(poseStack, buffer, combinedLight, player);

            poseStack.translate(-0.39, 0.775, 0);
            poseStack.scale(modifier, modifier, modifier);

            poseStack.mulPose(Axis.YP.rotationDegrees(180));
            poseStack.mulPose(Axis.XN.rotationDegrees(182F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(5));

        } else {
            renderer.renderLeftHand(poseStack, buffer, combinedLight, player);

            poseStack.translate(0.4, 0.775, -0.01);
            poseStack.scale(modifier, modifier, -modifier);

            poseStack.mulPose(Axis.XN.rotationDegrees(180F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(7));
        }

        new GeoRenderer(poseStack, buffer, assetsSample.getTexture(), assetsSample.getGeoModel(), overlay, combinedLight)
                .draw();

        poseStack.popPose();
    }
}
