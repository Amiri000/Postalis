package sad.ami.postalis.api.system.geo.manage;

import net.minecraft.resources.ResourceLocation;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.api.system.geo.util.RenderObjects;

public interface IGeoRendererManager extends IGeoRenderer {
    default GeoModel getGeoModel(RenderObjects type, String fileName) {
        return GeoModelManager.get(getModelPath(type, fileName));
    }

    default ResourceLocation getTexturePath(RenderObjects type, String fileName) {
        return ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "textures/" + type.getType() + "/" + fileName + ".png");
    }

    default ResourceLocation getModelPath(RenderObjects type, String fileName) {
        return ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "geo/" + type.getType() + "/" + fileName + ".geo.json");
    }
}
