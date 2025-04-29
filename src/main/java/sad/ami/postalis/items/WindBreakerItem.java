package sad.ami.postalis.items;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import sad.ami.postalis.init.PDataComponentRegistry;
import sad.ami.postalis.items.base.BaseSwordItem;
import sad.ami.postalis.items.base.IHoldTickItem;

import java.util.UUID;

public class WindBreakerItem extends BaseSwordItem implements IHoldTickItem {
    @Override
    public void inMainHand(Player player, ItemStack stack, Level level) {
        if (!level.isClientSide())
            return;

        var minecraft = Minecraft.getInstance();

        if (!minecraft.options.keyUse.isDown()
                || !(player.pick(player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE), 0.0F, false) instanceof BlockHitResult blockHitResult))
            return;

        var pos = blockHitResult.getBlockPos();
        var state = level.getBlockState(pos);
        var block = state.getBlock();

        var shape = state.getCollisionShape(level, pos);

        if (shape.isEmpty())
            return;

        var aabb = shape.bounds();

        double widthX = aabb.maxX - aabb.minX;
        double widthZ = aabb.maxZ - aabb.minZ;

        if (state.isAir() || Math.max(widthX, widthZ) < 0.5F)
            return;

    }

    @Override
    public void onHeldTickInMainHand(Player player, ItemStack stack, Level level, int tickCount) {
        System.out.println(tickCount);
    }

    public void saveUUID(ItemStack stack, UUID uuid) {
        stack.set(PDataComponentRegistry.STRING, uuid.toString());
    }

    public String getUUID(ItemStack stack) {
        return stack.getOrDefault(PDataComponentRegistry.STRING, "");
    }
}

