package sad.ami.postalis.items;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import sad.ami.postalis.items.base.BaseSwordItem;

public class WindBreakerItem extends BaseSwordItem {
    @Override
    public void inMainHand(Player player, ItemStack stack, Level level) {
        //        var minecraft = Minecraft.getInstance();
//
//        if (!minecraft.options.keyUse.isDown()
//                || !(player.pick(player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE), 0.0F, false) instanceof BlockHitResult blockHitResult))
//            return;
//
//        var pos = blockHitResult.getBlockPos();
//        var state = level.getBlockState(pos);
//        var block = state.getBlock();
//
//        var shape = state.getCollisionShape(level, pos);
//
//        if (shape.isEmpty())
//            return;
//
//        var aabb = shape.bounds();
//
//        double widthX = aabb.maxX - aabb.minX;
//        double widthZ = aabb.maxZ - aabb.minZ;
//
//        if (state.isAir() || Math.max(widthX, widthZ) < 0.5F)
//            return;

    }


//    @Override
//    static void onInteractionItem(Player caster, Level level, UseStage stage, int tickCount) {
//        if (level.isClientSide())
//            return;
//
//        for (var target : level.players())
//            if (!target.getUUID().equals(caster.getUUID()))
//                NetworkHandler.sendToClient(new BroadcastChargeTicksPacket(caster.getId(), tickCount), (ServerPlayer) target);
//
//        if (caster.getCommandSenderWorld() instanceof ServerLevel serverLevel)
//            for (ServerPlayer player : serverLevel.getChunkSource().chunkMap.getPlayers(caster.chunkPosition(), false))
//                NetworkHandler.sendToClient(new CastAnimationPacket(caster.getId(), true), player);
//    }
}

