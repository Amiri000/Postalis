package sad.ami.postalis.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import sad.ami.postalis.block.block_entity.HeavensForgeBlockEntity;
import sad.ami.postalis.client.screen.base.BaseScreen;
import sad.ami.postalis.init.PDataComponentRegistry;
import sad.ami.postalis.items.base.BaseSwordItem;
import sad.ami.postalis.items.base.interfaces.IBranchableItem;

public class UpgradeAbilityScreen extends BaseScreen {
    private final BlockPos pedestalPos;
    private final BlockState pedestalState;
    private final ItemStack pedestalItem;

    public UpgradeAbilityScreen(BlockPos pos) {
        super(Component.literal("upgrade_screen"));

        var level = Minecraft.getInstance().level;

        this.pedestalPos = pos;

        if (level != null) {
            this.pedestalState = level.getBlockState(pos);
            this.pedestalItem = level.getBlockEntity(pos) instanceof HeavensForgeBlockEntity forge && forge.getPedestalItem().getItem() instanceof BaseSwordItem
                    ? forge.getPedestalItem() : ItemStack.EMPTY;

        } else {
            this.pedestalState = null;
            this.pedestalItem = ItemStack.EMPTY;
        }
    }

    @Override
    public void tick() {
        var level = Minecraft.getInstance().level;

        if (level == null || pedestalItem == null || pedestalItem.isEmpty()) {
            this.onClose();

            return;
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);

        if (!(pedestalItem.getItem() instanceof IBranchableItem branchableItem))
            return;

        var text =pedestalItem.get(PDataComponentRegistry.SELECTED_BRANCH).getBranchTypes().toString();

        int x = this.width / 2 - font.width(text) / 2;
        int y = 20;

        graphics.drawString(font, text, x, y, 0xFFFFFF, false);
        graphics.drawString(font, pedestalItem.get(PDataComponentRegistry.SELECTED_BRANCH).getBranchSelected().getDisplayName(), x, y + 20, 0xFFFFFF, false);
    }

}
