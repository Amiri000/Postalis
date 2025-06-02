package sad.ami.postalis.api.system.geo;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class GeoModelManager {
    public static final Map<ResourceLocation, GeoModel> CACHE = new HashMap<>();

    public static void preload(ResourceLocation location) {
        if (!CACHE.containsKey(location)) {

            var model = GeoModelLoader.load(location);

            if (model != null)
                CACHE.put(location, model);
        }
    }

    public static GeoModel get(ResourceLocation location) {
        return CACHE.get(location);
    }
}
