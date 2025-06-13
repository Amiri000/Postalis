package sad.ami.postalis;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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

    public static void sssa(AbstractClientPlayer player, float partialTicks, float pitch, InteractionHand hand, float swingProgress, ItemStack stack, float equippedProgress, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, CallbackInfo ci) {
        if (Minecraft.getInstance().player.getMainHandItem().getItem() != ItemRegistry.ORNAMENT_GLOVE.get() || hand != InteractionHand.MAIN_HAND)
            return;

        poseStack.pushPose();

        var swingRoot = Mth.sqrt(swingProgress);

        var translateX = -0.3F * Mth.sin((float) (swingRoot * Math.PI)) + 0.64000005F;
        var translateY = 0.4F * Mth.sin((float) (swingRoot * Math.PI * 2.0)) - 0.6F + equippedProgress * -0.6F;
        var translateZ = -0.4F * Mth.sin((float) (swingProgress * Math.PI)) - 0.71999997F;

        poseStack.translate(translateX, translateY, translateZ);
        poseStack.mulPose(Axis.YP.rotationDegrees(45.0F));

        var swingSinSq = Mth.sin((float) (swingProgress * swingProgress * Math.PI));
        var swingSin = Mth.sin((float) (swingRoot * Math.PI));

        poseStack.mulPose(Axis.YP.rotationDegrees(swingSin * 70.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(swingSinSq * -20.0F));

        poseStack.translate(-1.0F, 3.6F, 3.5F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(120.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(200.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(-135.0F));
        poseStack.translate(5.6F, 0.0F, 0.0F);


        var renderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player);

        renderer.renderRightHand(poseStack, buffer, combinedLight, player);

        poseStack.popPose();
    }
}
