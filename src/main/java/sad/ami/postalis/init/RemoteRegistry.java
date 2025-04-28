package sad.ami.postalis.init;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.client.renderer.entities.EmbeddedSwordRenderer;
import sad.ami.postalis.client.renderer.entities.EmptyRenderer;

@EventBusSubscriber(modid = Postalis.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RemoteRegistry {
    @SubscribeEvent
    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.DESTRUCTIVE_TORNADO.get(), EmptyRenderer::new);
        event.registerEntityRenderer(EntityRegistry.EMBEDDED_SWORD.get(), EmbeddedSwordRenderer::new);
    }
}
