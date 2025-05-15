package sad.ami.postalis.items;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import sad.ami.postalis.api.event.PlayerItemInteractionEvent;
import sad.ami.postalis.client.interaction.ClientCastAnimation;
import sad.ami.postalis.entities.EmbeddedSwordEntity;
import sad.ami.postalis.items.base.BaseSwordItem;
import sad.ami.postalis.items.base.interfaces.IUsageItem;
import sad.ami.postalis.networking.NetworkHandler;
import sad.ami.postalis.networking.packets.sync.animations.CastAnimationPacket;

@EventBusSubscriber
public class WindBreakerItem extends BaseSwordItem implements IUsageItem {
    private static final int limitAnimationActivated = 3 * 20;
    private static BlockPos startCastPos;

    @SubscribeEvent
    public static void onInteraction(PlayerItemInteractionEvent event) {
        var level = event.getLevel();

        var caster = event.getCaster();
        var tickCount = event.getTickCount();
        var stage = event.getStage();

        if (!(caster.pick(caster.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE), 0.0F, false) instanceof BlockHitResult blockHitResult))
            return;

        var pos = blockHitResult.getBlockPos();

        if (tickCount == 1)
            startCastPos = pos;

        var state = level.getBlockState(pos);
        var block = state.getBlock();

        var shape = state.getCollisionShape(level, pos);

        if (shape.isEmpty() || stage == ClientCastAnimation.UseStage.TICK && startCastPos != null && !startCastPos.equals(pos)) {
            event.setCanceled(true);

            return;
        }

        var aabb = shape.bounds();

        double widthX = aabb.maxX - aabb.minX;
        double widthZ = aabb.maxZ - aabb.minZ;

        if (state.isAir() || Math.max(widthX, widthZ) < 0.5F || tickCount < limitAnimationActivated)
            return;

        var entity = new EmbeddedSwordEntity(level);

        entity.setPos(pos.above().getCenter());

        level.addFreshEntity(entity);

        caster.setItemInHand(caster.getUsedItemHand(), ItemStack.EMPTY);

        ClientCastAnimation.putChargeTicks(caster, 0);

        if (stage == ClientCastAnimation.UseStage.STOP)
            startCastPos = null;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onRenderUsage(ItemRenderer itemRenderer, Player player, ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource buffer, int light) {
        var chargeTicks = ClientCastAnimation.getChargeTicks(player);
        var mc = Minecraft.getInstance();

        if (chargeTicks == 1 || mc.isPaused())
            return;

        var time = chargeTicks + mc.getTimer().getGameTimeDeltaPartialTick(false);
        var amplitude = Math.min(((float) chargeTicks / 20) * 0.04F, 0.5f);

        var frequency = 0.25f;

        poseStack.translate(Math.sin(time * 2 * Math.PI * frequency) * amplitude, Math.cos(time * 2 * Math.PI * frequency * 0.5) * amplitude * 0.5f, 0);
    }
}

