package sad.ami.postalis.api.system.geo;

import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GeoModelLoader {
    public static GeoModel load(ResourceLocation location) {
        try {
            var optional = Minecraft.getInstance().getResourceManager().getResource(location);
            if (optional.isEmpty()) {
                System.err.println("GeoModel not found: " + location);

                return null;
            }

            try (InputStream stream = optional.get().open()) {
                var model = new Gson().fromJson(new InputStreamReader(stream), GeoModel.class);

                if (model == null)
                    System.err.println("GeoModel loaded but parsed as null: " + location);

                return model;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load geo model: " + location, e);
        }
    }
}
