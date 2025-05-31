package sad.ami.postalis.init;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import sad.ami.postalis.Postalis;

import java.io.IOException;

@EventBusSubscriber(modid = Postalis.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ShaderRegistry {
    public static ShaderInstance ORNAMENT_SHADER;

    @SubscribeEvent
    public static void init(RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(event.getResourceProvider(), ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "ornament"),
                DefaultVertexFormat.POSITION_TEX), shader -> ORNAMENT_SHADER = shader);
    }

}