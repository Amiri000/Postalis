package sad.ami.postalis.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import sad.ami.postalis.block.HeavensForgeBlock;
import sad.ami.postalis.block.block_entity.HeavensForgeBlockEntity;
import sad.ami.postalis.client.screen.base.BaseScreen;
import sad.ami.postalis.init.PDataComponentRegistry;
import sad.ami.postalis.items.base.BaseSwordItem;
import sad.ami.postalis.items.base.interfaces.IBranchableItem;

public class UpgradeAbilityScreen extends BaseScreen {
    private final BlockPos pedestalPos;
    private final BlockState pedestalState;

    public UpgradeAbilityScreen(BlockPos pos) {
        super(Component.literal("upgrade_screen"));

        this.pedestalPos = pos;
        this.pedestalState = Minecraft.getInstance().level != null ? Minecraft.getInstance().level.getBlockState(pos) : null;
    }

    @Override
    public void tick() {
        var level = Minecraft.getInstance().level;

        if (level == null || !(pedestalState.getBlock() instanceof HeavensForgeBlock heavensForgeBlock)
                || !(level.getBlockEntity(pedestalPos) instanceof HeavensForgeBlockEntity heavensForgeBlockEntity)
                || !(heavensForgeBlockEntity.getPedestalItem().getItem() instanceof BaseSwordItem item)) {
            this.onClose();

            return;
        }

        System.out.println(heavensForgeBlockEntity.getPedestalItem().get(PDataComponentRegistry.SELECTED_BRANCH));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);

    }

}
