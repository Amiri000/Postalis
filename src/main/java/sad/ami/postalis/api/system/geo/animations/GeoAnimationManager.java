package sad.ami.postalis.api.system.geo.animations;

import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class GeoAnimationManager {
    public static final Map<ResourceLocation, GeoAnimationContainer> CACHE = new HashMap<>();
    private static final Gson GSON = new Gson();

    public static void preload(ResourceLocation location) {
        if (!CACHE.containsKey(location)) {
            var anim = load(location);

            if (anim != null)
                CACHE.put(location, anim);
        }
    }

    private static GeoAnimationContainer load(ResourceLocation location) {
        try {
            var optional = Minecraft.getInstance().getResourceManager().getResource(location);

            if (optional.isEmpty())
                return null;

            try (InputStream stream = optional.get().open()) {
                return GSON.fromJson(new InputStreamReader(stream), GeoAnimationContainer.class);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to load geo animation: " + location, e);
        }
    }

    public static GeoAnimationContainer get(ResourceLocation location) {
        return CACHE.get(location);
    }
}
