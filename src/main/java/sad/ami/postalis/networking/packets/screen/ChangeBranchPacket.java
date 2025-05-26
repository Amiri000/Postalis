package sad.ami.postalis.networking.packets.screen;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.block.block_entity.HeavensForgeBlockEntity;
import sad.ami.postalis.items.base.BranchType;
import sad.ami.postalis.items.base.interfaces.IBranchableItem;
import sad.ami.postalis.networking.StreamCodecs;

public record ChangeBranchPacket(Vec3 blockPos, BranchType branchType) implements CustomPacketPayload {
    public static final Type<ChangeBranchPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "change_branch"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ChangeBranchPacket> STREAM_CODEC = StreamCodec.composite(
            StreamCodecs.VEC3_STREAM_CODEC, ChangeBranchPacket::blockPos,
            BranchType.STREAM_CODEC, ChangeBranchPacket::branchType,
            ChangeBranchPacket::new);

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            var level = ctx.player().getCommandSenderWorld();

            level.getBlockEntity(BlockPos.containing(blockPos));


            if (!(level.getBlockEntity(BlockPos.containing(blockPos)) instanceof HeavensForgeBlockEntity heavensForgeBlockEntity)
                    || !(heavensForgeBlockEntity.getPedestalItem().getItem() instanceof IBranchableItem branchableItem))
                return;

            var stateHeavensForge = heavensForgeBlockEntity.getBlockState();

            branchableItem.setSelectedBranch(heavensForgeBlockEntity.getPedestalItem(), branchType);

            level.sendBlockUpdated(heavensForgeBlockEntity.getBlockPos(), stateHeavensForge, stateHeavensForge, 3);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
