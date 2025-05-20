package sad.ami.postalis.block.block_entity;

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

public class HeavensForgeBlockEntity extends BlockEntity implements ITickableBlockEntity {
    private ItemStack display = ItemStack.EMPTY;

    public HeavensForgeBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntitiesRegistry.HEAVENS_FORGE.get(), pos, state);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
    }

    public ItemStack getItem() {
        return display;
    }

    public void setItem(ItemStack stack) {
        this.display = stack;

        if (level != null && !level.isClientSide)
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);

        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        if (!display.isEmpty())
            tag.put("Item", display.save(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        if (tag.contains("Item"))
            ItemStack.parse(registries, tag.getCompound("Item")).ifPresent(stack -> display = stack);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);

        if (!display.isEmpty())
            tag.put("Item", display.save(registries));

        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
