package sad.ami.postalis.items;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import sad.ami.postalis.api.interaction.ClientCastAnimation;
import sad.ami.postalis.items.base.BaseSwordItem;
import sad.ami.postalis.items.base.interfaces.IUsageItem;

public class WindBreakerItem extends BaseSwordItem implements IUsageItem {
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
    public void onUsage(Player caster, ClientCastAnimation.UseStage stage, Level level, int tickCount) {

    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onRenderUsage(ItemRenderer itemRenderer, Player player, ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource buffer, int light) {
        var chargeTicks = ClientCastAnimation.getChargeTicks(player);

        if (chargeTicks == 0)
            return;

        var time = chargeTicks + Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
        var amplitude = Math.min(((float) chargeTicks / 20) * 0.04F, 0.5f);

        var frequency = 0.25f;

        poseStack.translate(Math.sin(time * 2 * Math.PI * frequency) * amplitude, Math.cos(time * 2 * Math.PI * frequency * 0.5) * amplitude * 0.5f, 0);
    }
}

