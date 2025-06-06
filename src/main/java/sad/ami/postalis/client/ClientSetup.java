package sad.ami.postalis.client;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.api.system.geo.GeoBlockItemRenderer;
import sad.ami.postalis.api.system.geo.manage.GeoModelManager;
import sad.ami.postalis.init.BlockRegistry;

import java.util.Collection;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();

       var resources = resourceManager.listResources("geo/block", path -> path.getPath().endsWith(".geo.json"));

        for (ResourceLocation loc : resources.keySet()) {
            System.out.println("fqafqsaf: " + loc);
        }

        event.enqueueWork(() -> GeoModelManager.preload(ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "geo/test_model.geo.json")));
    }

    @SubscribeEvent
    public static void registerClientItemExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new GeoBlockItemRenderer(), BlockRegistry.HEAVENS_FORGE.get().asItem());
    }
}
