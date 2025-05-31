package sad.ami.postalis.client;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.api.system.geo.GeoModelManager;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            GeoModelManager.preload(ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "geo/test_model.geo.json"));
        });
    }
}
