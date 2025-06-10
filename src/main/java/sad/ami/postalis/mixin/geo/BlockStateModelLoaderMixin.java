package sad.ami.postalis.mixin.geo;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.BlockModelDefinition;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sad.ami.postalis.Postalis;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Mixin(BlockStateModelLoader.class)
public class BlockStateModelLoaderMixin {
    @Inject(method = "loadBlockStateDefinitions", at = @At("HEAD"), cancellable = true)
    private void postalis$injectVariantJson(ResourceLocation id, StateDefinition<Block, BlockState> definition, CallbackInfo ci) {
        if (!id.getNamespace().equals(Postalis.MODID))
            return;

        var model = new BlockModel(ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "block/" + id.getPath()), List.of(), Map.of(), true, BlockModel.GuiLight.SIDE, ItemTransforms.NO_TRANSFORMS, List.of());
        var loadedModel = new BlockStateModelLoader.LoadedModel(model, () -> new BlockStateModelLoader.ModelGroupKey(List.of(model), List.of()));

        for (BlockState state : definition.getPossibleStates()) {
            discoveredModelOutput.accept( BlockModelShaper.stateToModelLocation(id, state), loadedModel.model());
            modelGroups.put(state, 1);
        }

        ci.cancel();
    }

    @Final
    @Shadow
    private BiConsumer<ModelResourceLocation, UnbakedModel> discoveredModelOutput;

    @Final
    @Shadow
    private Object2IntMap<BlockState> modelGroups;
}