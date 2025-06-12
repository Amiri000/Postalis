package sad.ami.postalis.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import sad.ami.postalis.api.system.geo.manage.IGeoRenderer;
import sad.ami.postalis.api.system.geo.samples.ResourceAssetsSample;

public class OrnamentGloveRenderer extends BlockEntityWithoutLevelRenderer implements IGeoRenderer {
    private final ResourceLocation model;
    private final ResourceLocation texture;

    public OrnamentGloveRenderer(ResourceAssetsSample sample) {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());

        this.model = sample.getModel();
        this.texture = sample.getTexture();
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        super.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
    }


}
