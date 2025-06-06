package sad.ami.postalis.mixin.geo;

import com.mojang.datafixers.util.Either;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
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
    private void injectFakeModelForBlockItem(ResourceLocation modelLocation, CallbackInfo ci) {
        if (!modelLocation.getNamespace().equals(Postalis.MODID) || !modelLocation.getPath().equals("heavens_forge"))
            return;

        var fakeModel = new BlockModel(ResourceLocation.withDefaultNamespace("builtin/entity"), List.of(), Map.of("particle",
                Either.right("postalis:block/texture")), null, BlockModel.GuiLight.SIDE, ItemTransforms.NO_TRANSFORMS, List.of());

        registerModel(ModelResourceLocation.inventory(modelLocation), fakeModel);

        ci.cancel();
    }

    @Inject(method = "loadBlockModel", at = @At("HEAD"), cancellable = true)
    private void postalis$injectBlockModel(ResourceLocation location, CallbackInfoReturnable<BlockModel> cir) {
        if (!location.getNamespace().equals(Postalis.MODID) || !location.getPath().equals("block/heavens_forge"))
            return;

        Map<String, Either<Material, String>> textureMap = Map.of("particle", Either.left(new Material(TextureAtlas.LOCATION_BLOCKS,
                ResourceLocation.fromNamespaceAndPath("postalis", "block/texture"))));

        BlockModel model = new BlockModel(null, List.of(), textureMap, true, BlockModel.GuiLight.SIDE, ItemTransforms.NO_TRANSFORMS, List.of());

        cir.setReturnValue(model);
    }

    @Shadow
    private void registerModel(ModelResourceLocation modelLocation, UnbakedModel model) {
    }
}
