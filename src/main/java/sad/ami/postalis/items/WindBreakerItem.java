package sad.ami.postalis.items;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import sad.ami.postalis.api.event.PlayerItemInteractionEvent;
import sad.ami.postalis.client.interaction.ClientCastAnimation;
import sad.ami.postalis.entities.EmbeddedSwordEntity;
import sad.ami.postalis.init.PDataComponentRegistry;
import sad.ami.postalis.items.base.BaseSwordItem;
import sad.ami.postalis.items.base.BranchType;
import sad.ami.postalis.items.base.BranchesData;

import java.util.Set;

@EventBusSubscriber
public class WindBreakerItem extends BaseSwordItem {
    private static float smoothCharge = 0;

    public WindBreakerItem() {
        super(BranchesData.builder()
                .branchTypes(Set.of(BranchType.WIND, BranchType.STORM))
                .build());
    }

    @SubscribeEvent
    public static void onInteraction(PlayerItemInteractionEvent event) {
        var level = event.getLevel();
        var caster = event.getCaster();
        var blockPos = event.getTargetBlockPos();

        var entity = new EmbeddedSwordEntity(level);

        entity.setPos(blockPos.above().getCenter());

        level.addFreshEntity(entity);

        caster.setItemInHand(caster.getUsedItemHand(), ItemStack.EMPTY);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onRenderUsage(ItemRenderer itemRenderer, Player player, ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource buffer, int light) {
        var mc = Minecraft.getInstance();

        var level = mc.level;
        var chargeTicks = ClientCastAnimation.getChargeTicks(player);

        if (level == null || chargeTicks < 1)
            return;

        smoothCharge += (chargeTicks - smoothCharge) * 0.2F;

        var time = (level.getGameTime() + mc.getTimer().getGameTimeDeltaPartialTick(false)) * 0.25f;
        var amplitude = Math.min((smoothCharge / 20f) * 0.04f, 0.5f);

        poseStack.translate(Math.sin(time * 2 * Math.PI) * amplitude, Math.cos(time * 2 * Math.PI * 0.5f) * amplitude * 0.5f, 0);
    }
}

