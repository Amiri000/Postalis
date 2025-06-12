package sad.ami.postalis.client.renderer.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import sad.ami.postalis.api.system.geo.manage.IGeoRenderer;

public class OrnamentGloveRenderer extends BlockEntityWithoutLevelRenderer implements IGeoRenderer {
    public static final OrnamentGloveRenderer INSTANCE = new OrnamentGloveRenderer(
            Minecraft.getInstance().getBlockEntityRenderDispatcher(),
            Minecraft.getInstance().getEntityModels()
    );

    public OrnamentGloveRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
        super(blockEntityRenderDispatcher, entityModelSet);
    }

}
