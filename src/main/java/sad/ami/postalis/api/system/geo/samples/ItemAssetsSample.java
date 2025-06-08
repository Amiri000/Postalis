package sad.ami.postalis.api.system.geo.samples;

import lombok.Data;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.api.system.geo.manage.GeoModel;
import sad.ami.postalis.api.system.geo.manage.GeoModelManager;
import sad.ami.postalis.api.system.geo.util.RenderObjects;

@Data
public class ItemAssetsSample {
    private ResourceLocation model;
    private ResourceLocation texture;

    public ItemAssetsSample(Block block) {
        var name = BuiltInRegistries.BLOCK.getKey(block).getPath();

        this.model = ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "geo/models/block/" + name + ".geo.json");
        this.texture =  ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "textures/block/" + name + ".png");
    }

    public ItemAssetsSample(Item item) {
        var name = BuiltInRegistries.ITEM.getKey(item).getPath();

        this.model = ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "geo/models/item/" + name + ".geo.json");
        this.texture =  ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "textures/item/" + name + ".png");
    }
}
