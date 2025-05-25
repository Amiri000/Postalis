package sad.ami.postalis.client.screen.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import sad.ami.postalis.items.base.BranchType;
import sad.ami.postalis.networking.NetworkHandler;
import sad.ami.postalis.networking.packets.screen.ChangeBranchPacket;

public class BranchButton extends AbstractWidget {
    private final BlockPos pedestalPos;

    public BranchButton(int x, int y, int width, int height, Component message, BlockPos blockPos) {
        super(x, y, width, height, message);

        this.pedestalPos = blockPos;
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        super.onClick(mouseX, mouseY, button);

        NetworkHandler.sendToServer(new ChangeBranchPacket(pedestalPos.getCenter(), BranchType.STORM));
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int bgColor = this.isHoveredOrFocused() ? 0xFF444444 : 0xFF222222;
        int borderColor = 0xFFFFFFFF;

        guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), bgColor);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, this.getMessage().getString(), this.getX() + this.getWidth() / 2, this.getY() + (this.getHeight() - 8) / 2, borderColor);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
