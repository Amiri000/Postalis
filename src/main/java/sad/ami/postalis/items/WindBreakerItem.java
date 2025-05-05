package sad.ami.postalis.items;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import sad.ami.postalis.items.base.BaseSwordItem;
import sad.ami.postalis.items.base.interfaces.IHoldTickItem;
import sad.ami.postalis.networking.NetworkHandler;
import sad.ami.postalis.networking.packets.CastAnimationPacket;

public class WindBreakerItem extends BaseSwordItem implements IHoldTickItem {
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

    @Override
    public void onHeldTickInMainHand(Player caster, Level level, int tickCount) {
        if (caster.getCommandSenderWorld() instanceof ServerLevel serverLevel)
            for (ServerPlayer player : serverLevel.getChunkSource().chunkMap.getPlayers(caster.chunkPosition(), false))
                NetworkHandler.sendToClient(new CastAnimationPacket(caster.getId(), true), player);
    }
}

