package sad.ami.postalis.mixin;

import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sad.ami.postalis.Postalis;

@Mixin(BlockStateModelLoader.class)
public class MixinBlockStateModelLoader {
    @Inject(method = "loadBlockStateDefinitions", at = @At("HEAD"), cancellable = true)
    private void postalis$skipBlockstateLoading(ResourceLocation id, StateDefinition<Block, BlockState> definition, CallbackInfo ci) {
        if (id.getNamespace().equals(Postalis.MODID)) {
            ci.cancel();
        }
    }
}