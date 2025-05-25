package sad.ami.postalis.block.block_entity;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import sad.ami.postalis.init.BlockEntitiesRegistry;

import javax.annotation.Nullable;

@Getter
public class HeavensForgeBlockEntity extends BlockEntity implements ITickableBlockEntity {
    private ItemStack pedestalItem = ItemStack.EMPTY;

    public HeavensForgeBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntitiesRegistry.HEAVENS_FORGE.get(), pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
//        if(!pedestalItem.isEmpty() && level.isClientSide)
//            NetworkHandler.sendToServer(new ChangeBranchPacket(this.getBlockPos().getCenter(), BranchType.STORM));

        // System.out.println(pedestalItem.get(PDataComponentRegistry.SELECTED_BRANCH));
    }

    public void setItem(ItemStack stack) {
        this.pedestalItem = stack;

        if (level != null && !level.isClientSide)
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);

        setChanged();
    }

    public boolean hasItem() {
        return !getPedestalItem().isEmpty();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        if (!pedestalItem.isEmpty())
            tag.put("Item", pedestalItem.save(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        if (tag.contains("Item"))
            ItemStack.parse(registries, tag.getCompound("Item")).ifPresent(stack -> pedestalItem = stack);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);

        if (!pedestalItem.isEmpty())
            tag.put("Item", pedestalItem.save(registries));

        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
