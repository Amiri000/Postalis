package sad.ami.postalis.items.base.interfaces;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import sad.ami.postalis.init.PDataComponentRegistry;
import sad.ami.postalis.items.base.BranchType;
import sad.ami.postalis.data.BranchesData;

import java.util.Set;

public interface IBranchableItem {
    default Set<BranchType> getBranchTypes(ItemStack stack) {
        return getBranchesData(stack).getBranchTypes();
    }

    default void setSelectedBranch(ItemStack stack, BranchType branchType) {
        setBranchesData(stack, getBranchesData(stack).toBuilder().branchSelected(branchType).build());
    }

    default void setBranchesData(ItemStack stack, BranchesData branchesData) {
        stack.set(PDataComponentRegistry.SELECTED_BRANCH, branchesData);
        System.out.println(stack.get(PDataComponentRegistry.SELECTED_BRANCH));
    }

    default BranchType getBranchSelected(ItemStack stack) {
        return getBranchesData(stack).getBranchSelected();
    }

    default BranchesData getBranchesData(ItemStack stack) {
        return stack.getOrDefault(PDataComponentRegistry.SELECTED_BRANCH, BranchesData.DEFAULT);
    }

    @OnlyIn(Dist.CLIENT)
    default void onRenderUsage(ItemRenderer itemRenderer, Player player, ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource buffer, int light) {
    }
}
