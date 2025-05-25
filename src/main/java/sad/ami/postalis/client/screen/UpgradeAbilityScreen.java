package sad.ami.postalis.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import sad.ami.postalis.block.block_entity.HeavensForgeBlockEntity;
import sad.ami.postalis.client.screen.base.BaseScreen;
import sad.ami.postalis.client.screen.widgets.BranchButton;
import sad.ami.postalis.items.base.interfaces.IBranchableItem;

public class UpgradeAbilityScreen extends BaseScreen {
    private final BlockPos pedestalPos;
    private final BlockState pedestalState;

    public UpgradeAbilityScreen(BlockPos pos) {
        super(Component.literal("upgrade_screen"));
        var level = Minecraft.getInstance().level;
        this.pedestalPos = pos;
        this.pedestalState = level != null ? level.getBlockState(pos) : null;
    }

    @Override
    protected void init() {
        super.init();

        int x = this.width / 2 - 40;
        int y = this.height / 2;

        this.addRenderableWidget(new BranchButton(x, y, 80, 20, Component.literal("Выбрать ветвь"), pedestalPos));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);

        var level = Minecraft.getInstance().level;
        if (level == null) return;

        BlockEntity be = level.getBlockEntity(pedestalPos);
        if (!(be instanceof HeavensForgeBlockEntity forge)) return;

        ItemStack pedestalItem = forge.getPedestalItem();
        if (!(pedestalItem.getItem() instanceof IBranchableItem branchableItem)) return;

        int x = this.width / 2 - 50 / 2;
        int y = 20;

        graphics.drawString(font, branchableItem.getBranchTypes(pedestalItem).toString(), x, y, 0xFFFFFF, false);
        graphics.drawString(font, branchableItem.getBranchSelected(pedestalItem).getDisplayName(), x, y + 20, 0xFFFFFF, false);
    }
}
