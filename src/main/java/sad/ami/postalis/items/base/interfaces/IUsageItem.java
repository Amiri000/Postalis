package sad.ami.postalis.items.base.interfaces;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import sad.ami.postalis.client.interaction.ClientCastAnimation;

public interface IUsageItem {
    void onUsage(Player caster, ClientCastAnimation.UseStage stage, Level level, int tickCount);

    @OnlyIn(Dist.CLIENT)
    default void onRenderUsage(ItemRenderer itemRenderer, Player player, ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource buffer, int light) {

    }
}
