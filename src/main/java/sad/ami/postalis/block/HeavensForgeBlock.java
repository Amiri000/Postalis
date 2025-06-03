package sad.ami.postalis.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;
import org.jetbrains.annotations.Nullable;
import sad.ami.postalis.block.block_entity.HeavensForgeBlockEntity;
import sad.ami.postalis.block.block_entity.ITickableBlockEntity;
import sad.ami.postalis.init.BlockEntitiesRegistry;
import sad.ami.postalis.init.PDataComponentRegistry;
import sad.ami.postalis.networking.NetworkHandler;
import sad.ami.postalis.networking.packets.screen.OpenScreenPacket;

import java.util.function.Consumer;

public class HeavensForgeBlock extends BaseEntityBlock {
    public HeavensForgeBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.MAIN_HAND || !(level.getBlockEntity(pos) instanceof HeavensForgeBlockEntity forgeBlockEntity))
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;

        if (forgeBlockEntity.hasItem()) {
            if (stack.isEmpty() && player.isShiftKeyDown()) {
                player.setItemInHand(hand, forgeBlockEntity.getPedestalItem());

                forgeBlockEntity.setItem(ItemStack.EMPTY);

                return ItemInteractionResult.SUCCESS;
            }

            if (!level.isClientSide())
                NetworkHandler.sendToClient(new OpenScreenPacket(pos), (ServerPlayer) player);
        } else {
            if (stack.has(PDataComponentRegistry.SELECTED_BRANCH)) {
                forgeBlockEntity.setItem(stack.copyWithCount(1));

                stack.shrink(1);
            }
        }

        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);

            if (be instanceof HeavensForgeBlockEntity forge && !forge.getPedestalItem().isEmpty()) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), forge.getPedestalItem());

                forge.setItem(ItemStack.EMPTY);
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == BlockEntitiesRegistry.HEAVENS_FORGE.get() ? (lvl, pos, st, be) -> ((HeavensForgeBlockEntity) be).tick(lvl, pos, st) : null;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HeavensForgeBlockEntity(pos, state);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(HeavensForgeBlock::new);
    }
}
