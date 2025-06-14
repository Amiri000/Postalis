package sad.ami.postalis.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.api.system.geo.GeoBlockItemRenderer;
import sad.ami.postalis.api.system.geo.animations.GeoAnimationManager;
import sad.ami.postalis.api.system.geo.manage.GeoModelManager;
import sad.ami.postalis.api.system.geo.manage.IGeoRendererManager;
import sad.ami.postalis.api.system.geo.samples.ResourceAssetsSample;
import sad.ami.postalis.client.renderer.block_entity.HeavensForgeRenderer;
import sad.ami.postalis.client.renderer.entities.EmbeddedSwordRenderer;
import sad.ami.postalis.client.renderer.entities.EmptyRenderer;
import sad.ami.postalis.client.renderer.entities.OrnamentRenderer;
import sad.ami.postalis.client.renderer.laeyrs.GloveLayer;

import java.util.List;

@EventBusSubscriber(modid = Postalis.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RemoteRegistry {
    @SubscribeEvent
    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.DESTRUCTIVE_TORNADO.get(), EmptyRenderer::new);
        event.registerEntityRenderer(EntityRegistry.EMBEDDED_SWORD.get(), EmbeddedSwordRenderer::new);
        event.registerEntityRenderer(EntityRegistry.ORNAMENT.get(), OrnamentRenderer::new);

        event.registerBlockEntityRenderer(BlockEntitiesRegistry.HEAVENS_FORGE.get(), c -> new HeavensForgeRenderer());
    }

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        PlayerRenderer defaultRenderer = event.getSkin(PlayerSkin.Model.WIDE);

        if (defaultRenderer != null) {
            defaultRenderer.addLayer(new GloveLayer(defaultRenderer));
        }

        PlayerRenderer slimRenderer = event.getSkin(PlayerSkin.Model.SLIM);

        if (slimRenderer != null) {
            slimRenderer.addLayer(new GloveLayer(slimRenderer));
        }
    }

    @SubscribeEvent
    public static void registerClientItemExtensions(RegisterClientExtensionsEvent event) {
        for (var holder : ItemRegistry.ITEMS.getEntries()) {
            var item = holder.get();

            if (!(item instanceof IGeoRendererManager geoObject))
                continue;

            if (geoObject.getCustomRender() != null)
                event.registerItem(geoObject.getCustomRender(), item);

            if (geoObject.getBlock() != null)
                event.registerItem(new GeoBlockItemRenderer(new ResourceAssetsSample(geoObject.getBlock())), item);
        }
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        var resourceManager = Minecraft.getInstance().getResourceManager();

        var animationPath = ResourceLocation.fromNamespaceAndPath("postalis", "geo/animations/heavens_forge/unknown.animation.json");
        GeoAnimationManager.preload(animationPath);

        for (var folder : List.of("geo/models/block", "geo/models/item"))
            for (var loc : resourceManager.listResources(folder, path -> path.getPath().endsWith(".geo.json")).keySet())
                event.enqueueWork(() -> GeoModelManager.preload(loc));
    }
}