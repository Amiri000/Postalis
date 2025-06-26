package sad.ami.postalis.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import sad.ami.postalis.api.system.geo.manage.IGeoRendererManager;
import sad.ami.postalis.api.system.geo.samples.ResourceAssetsSample;
import sad.ami.postalis.client.renderer.item.BewitchedGauntletRenderer;
import sad.ami.postalis.entities.MagicSealEntity;
import sad.ami.postalis.init.PDataComponentRegistry;

import java.util.List;
import java.util.UUID;

public class BewitchedGauntletItem extends Item implements IGeoRendererManager {
    private static final int useDurationTick = 20;
    private BlockPos lastTargetedBlock = null;
    private int holdTicks = 0;

    public BewitchedGauntletItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (!(livingEntity instanceof Player player) || level.isClientSide())
            return;

        var reach = player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE);

        if (!(player.pick(reach, 0.0F, false) instanceof BlockHitResult blockHit))
            return;

        var pos = blockHit.getBlockPos();

        if (level.getBlockState(pos).isAir()) {
            player.stopUsingItem();

            return;
        }

        if (pos.equals(lastTargetedBlock))
            holdTicks++;
        else
            lastTargetedBlock = pos;

        if (holdTicks >= useDurationTick) {
            var magicSeal = new MagicSealEntity(player.level());

            magicSeal.setPos(pos.getCenter().add(0, 0.35, 0));

            level.addFreshEntity(magicSeal);

            saveUUIDSeal(stack, magicSeal.getUUID());

            player.stopUsingItem();
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var level = context.getLevel();
        var player = context.getPlayer();

        if (player != null && !level.isClientSide() && !hasSeal(context.getItemInHand()))
            player.startUsingItem(context.getHand());

        return InteractionResult.CONSUME;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        var sealUUID = getSealUUID(stack);

        if (level.isClientSide())
            return;

        if (((ServerLevel) level).getEntity(sealUUID) == null) {
            setSealPresence(stack, false);
            saveUUIDSeal(stack, null);
        } else {
            if (hasSeal(stack))
                return;

            setSealPresence(stack, true);
        }
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        if (!(entity instanceof Player player))
            return;

        player.getCooldowns().addCooldown(this, 60);

        lastTargetedBlock = null;
        holdTicks = 0;
    }

    public void setSealPresence(ItemStack stack, boolean flag) {
        stack.set(PDataComponentRegistry.BOOLEAN, flag);
    }

    public boolean hasSeal(ItemStack stack) {
        return stack.getOrDefault(PDataComponentRegistry.BOOLEAN, false);
    }

    public void saveUUIDSeal(ItemStack stack, UUID uuid) {
        stack.set(PDataComponentRegistry.UUID, uuid);
    }

    public UUID getSealUUID(ItemStack stack) {
        return stack.get(PDataComponentRegistry.UUID);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public IClientItemExtensions getCustomRender() {
        return new BewitchedGauntletRenderer(new ResourceAssetsSample(this));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.empty());

        tooltipComponents.add(Component.translatable("tooltip.postalis.ornament_glove").withStyle(ChatFormatting.GRAY));
    }
}
