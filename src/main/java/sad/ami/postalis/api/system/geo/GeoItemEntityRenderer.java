package sad.ami.postalis.api.system.geo;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class GeoItemEntityRenderer extends BlockEntityWithoutLevelRenderer implements IClientItemExtensions {
    public GeoItemEntityRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public final void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        if (displayContext == ItemDisplayContext.GROUND) {
            float modifier = 1f / 60f;

            poseStack.scale(modifier, modifier, modifier);
            poseStack.translate(30, 23, 30);
        }

        this.renderItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);

        poseStack.popPose();
    }

    protected void renderItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

    }

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return this;
    }
}
