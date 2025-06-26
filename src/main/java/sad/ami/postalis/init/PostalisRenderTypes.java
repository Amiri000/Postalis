package sad.ami.postalis.init;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import sad.ami.postalis.Postalis;

import static net.minecraft.client.renderer.RenderStateShard.*;

public class PostalisRenderTypes {
    public static final RenderType SEAL_RENDER_TYPE = RenderType.create("postalis:ornament", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState
            .builder().setShaderState(new RenderStateShard.ShaderStateShard(() -> ShaderRegistry.ORNAMENT_SHADER)).setTextureState(new RenderStateShard.TextureStateShard(ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "textures/entities/ornament.png"), false, false))
            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
            .setDepthTestState(LEQUAL_DEPTH_TEST)
            .setWriteMaskState(COLOR_DEPTH_WRITE)
            .setCullState(NO_CULL)
            .createCompositeState(false)
    );
}
