package sad.ami.postalis.items;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import sad.ami.postalis.api.event.RendererItemInHandEvent;
import sad.ami.postalis.client.interaction.ClientCastAnimation;
import sad.ami.postalis.items.base.BaseSwordItem;
import sad.ami.postalis.items.base.interfaces.IUsageItem;
import sad.ami.postalis.networking.NetworkHandler;
import sad.ami.postalis.networking.packets.sync.animations.CastAnimationPacket;

public class WindBreakerItem extends BaseSwordItem implements IUsageItem {
    private static final int limitAnimationActivated = 3 * 20;

    @Override
    public void inMainHand(Player player, ItemStack stack, Level level) {
        if (level.isClientSide())
            return;
//          if (player.getCommandSenderWorld() instanceof ServerLevel serverLevel)
//            for (ServerPlayer playerServer : serverLevel.getChunkSource().chunkMap.getPlayers(player.chunkPosition(), false))

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
    public void onUsage(Player caster, ClientCastAnimation.UseStage stage, Level level, int tickCount) {
        if (tickCount < limitAnimationActivated)
            return;

        for (ServerPlayer player : ((ServerLevel) level).getChunkSource().chunkMap.getPlayers(caster.chunkPosition(), false))
            NetworkHandler.sendToClient(new CastAnimationPacket(caster.getId()), player);

        caster.setItemInHand(caster.getUsedItemHand(), ItemStack.EMPTY);
        ClientCastAnimation.putChargeTicks(caster, 0);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onRenderUsage(ItemRenderer itemRenderer, Player player, ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource buffer, int light) {
        var chargeTicks = ClientCastAnimation.getChargeTicks(player);
        var mc = Minecraft.getInstance();

        if (chargeTicks == 0 || mc.isPaused())
            return;

        var time = chargeTicks + mc.getTimer().getGameTimeDeltaPartialTick(false);
        var amplitude = Math.min(((float) chargeTicks / 20) * 0.04F, 0.5f);

        var frequency = 0.25f;

        poseStack.translate(Math.sin(time * 2 * Math.PI * frequency) * amplitude, Math.cos(time * 2 * Math.PI * frequency * 0.5) * amplitude * 0.5f, 0);
    }
}

