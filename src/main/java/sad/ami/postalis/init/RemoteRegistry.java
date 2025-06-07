package sad.ami.postalis.init;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.api.system.geo.GeoBlockItemRenderer;
import sad.ami.postalis.api.system.geo.manage.GeoModelManager;
import sad.ami.postalis.client.renderer.block_entity.HeavensForgeRenderer;
import sad.ami.postalis.client.renderer.entities.EmbeddedSwordRenderer;
import sad.ami.postalis.client.renderer.entities.EmptyRenderer;
import sad.ami.postalis.client.renderer.entities.OrnamentRenderer;

@EventBusSubscriber(modid = Postalis.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RemoteRegistry {
    @SubscribeEvent
    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.DESTRUCTIVE_TORNADO.get(), EmptyRenderer::new);
        event.registerEntityRenderer(EntityRegistry.EMBEDDED_SWORD.get(), EmbeddedSwordRenderer::new);
        event.registerEntityRenderer(EntityRegistry.ORNAMENT.get(), OrnamentRenderer::new);

        event.registerBlockEntityRenderer(BlockEntitiesRegistry.HEAVENS_FORGE.get(), _ -> new HeavensForgeRenderer());
    }

    @SubscribeEvent
    public static void registerClientItemExtensions(RegisterClientExtensionsEvent event) {
        var block = BlockRegistry.HEAVENS_FORGE.get();

        event.registerItem(new GeoBlockItemRenderer(block), block.asItem());
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        var resourceManager = Minecraft.getInstance().getResourceManager();

        for (var loc : resourceManager.listResources("geo/block", path -> path.getPath().endsWith(".geo.json")).keySet())
            event.enqueueWork(() -> GeoModelManager.preload(loc));
    }
}