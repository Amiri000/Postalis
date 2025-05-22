package sad.ami.postalis.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import sad.ami.postalis.block.HeavensForgeBlock;
import sad.ami.postalis.block.block_entity.HeavensForgeBlockEntity;
import sad.ami.postalis.client.screen.base.IPostalisScreen;

public class UpgradeAbilityScreen extends Screen implements IPostalisScreen {
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

        if (level == null || !(pedestalState.getBlock() instanceof HeavensForgeBlock heavensForgeBlock) || pedestalPos == null
                || !(level.getBlockEntity(pedestalPos) instanceof HeavensForgeBlockEntity heavensForgeBlockEntity)) {
            this.onClose();

            return;
        }

    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);

    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
