package sad.ami.postalis.api.system.geo.renderer_type;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import sad.ami.postalis.api.system.geo.GeoModel;
import sad.ami.postalis.api.system.geo.GeoModelManager;
import sad.ami.postalis.api.system.geo.model_data.GeoRenderer;

public class GeoItemRenderer extends BlockEntityWithoutLevelRenderer implements GeoRenderer {
    private final ResourceLocation model;
    private final ResourceLocation texture;

    public GeoItemRenderer(ResourceLocation model, ResourceLocation texture) {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());

        this.model = model;
        this.texture = texture;
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack pose, MultiBufferSource buf, int light, int overlay) {
        GeoModel geo = GeoModelManager.CACHE.get(model);
        if (geo == null || geo.minecraft_geometry.isEmpty()) return;

        pose.pushPose();
        pose.translate(0.5, 0.5, 0.5);
        pose.scale(1f / 16f, 1f / 16f, 1f / 16f);

        VertexConsumer buffer = buf.getBuffer(RenderType.entityCutout(texture));

        var first = geo.minecraft_geometry.getFirst();
        for (var bone : first.bones)
            for (var cube : bone.cubes)
                drawCube(pose, buffer, cube, first.description.texture_width, first.description.texture_height, overlay, light);

        pose.popPose();
    }
}
