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
    private final BranchType branchType;

    public BranchButton(int x, int y, int width, int height, Component message, BlockPos blockPos, BranchType branchType) {
        super(x, y, width, height, message);

        this.pedestalPos = blockPos;
        this.branchType = branchType;
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        super.onClick(mouseX, mouseY, button);

        NetworkHandler.sendToServer(new ChangeBranchPacket(pedestalPos.getCenter(), branchType));
    }
    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int bgColor = this.isHoveredOrFocused() ? 0xFF444444 : 0xFF222222;
        int borderColor = 0xFFFFFFFF;

        // Рисуем фон
        guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), bgColor);

        // Получаем текст и шрифт
        var font = Minecraft.getInstance().font;
        String text = this.getMessage().getString();

        // Координаты для центрирования текста
        int textWidth = font.width(text);
        int textX = this.getX() + (this.getWidth() - textWidth) / 2;
        int textY = this.getY() + (this.getHeight() - font.lineHeight) / 2;

        // Рисуем текст
        guiGraphics.drawString(font, text, textX, textY, borderColor, false);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
