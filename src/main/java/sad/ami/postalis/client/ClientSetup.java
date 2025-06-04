package sad.ami.postalis.client;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.api.system.geo.GeoModelManager;
import sad.ami.postalis.api.system.geo.renderer_type.GeoItemRenderer;
import sad.ami.postalis.init.BlockRegistry;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> GeoModelManager.preload(ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "geo/test_model.geo.json")));
    }

    @SubscribeEvent
    public static void registerClientItemExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new GeoItemRenderer(ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "geo/test_model.geo.json"),
                        ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "textures/block/texture.png"));
            }
        }, BlockRegistry.HEAVENS_FORGE.get().asItem());
    }
}
