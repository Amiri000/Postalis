package sad.ami.postalis.api.system.geo;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class GeoModelManager {
    public static final Map<ResourceLocation, GeoModel> CACHE = new HashMap<>();

    public static void preload(ResourceLocation location) {
        if (!CACHE.containsKey(location)) {
            GeoModel model = GeoModelLoader.load(location);
            if (model != null) {
                System.out.println("PUT!!");
                CACHE.put(location, model);
            }
        }
    }

    public static GeoModel get(ResourceLocation location) {
        return CACHE.get(location);
    }
}
