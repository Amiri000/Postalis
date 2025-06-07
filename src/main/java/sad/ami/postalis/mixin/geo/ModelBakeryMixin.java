package sad.ami.postalis.mixin.geo;

import com.mojang.datafixers.util.Either;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sad.ami.postalis.Postalis;

import java.util.List;
import java.util.Map;

@Mixin(ModelBakery.class)
public class ModelBakeryMixin {
    @Inject(method = "loadItemModelAndDependencies", at = @At("HEAD"), cancellable = true)
    private void injectFakeModelForBlockItem(ResourceLocation location, CallbackInfo ci) {
        if (!location.getNamespace().equals(Postalis.MODID))
            return;

        var blockName = BuiltInRegistries.BLOCK.get(location).getName().getString();

        if (blockName.equalsIgnoreCase("air"))
            return;

        var parts = blockName.split("\\.");
        var fakeModel = new BlockModel(ResourceLocation.withDefaultNamespace("builtin/entity"), List.of(), Map.of("particle",
                Either.right(parts[1] + ":models/block/" + parts[2])), null, BlockModel.GuiLight.SIDE, ItemTransforms.NO_TRANSFORMS, List.of());

        registerModel(ModelResourceLocation.inventory(location), fakeModel);

        ci.cancel();
    }

    @Inject(method = "loadBlockModel", at = @At("HEAD"), cancellable = true)
    private void postalis$injectBlockModel(ResourceLocation location, CallbackInfoReturnable<BlockModel> cir) {
        if (!location.getNamespace().equals(Postalis.MODID) || !location.getPath().contains("block"))
            return;

        Map<String, Either<Material, String>> textureMap = Map.of("particle", Either.left(new Material(TextureAtlas.LOCATION_BLOCKS,
                ResourceLocation.fromNamespaceAndPath(Postalis.MODID, location.getPath()))));

        cir.setReturnValue(new BlockModel(null, List.of(), textureMap, true, BlockModel.GuiLight.SIDE, ItemTransforms.NO_TRANSFORMS, List.of()));
    }

    @Shadow
    private void registerModel(ModelResourceLocation modelLocation, UnbakedModel model) {
    }
}
