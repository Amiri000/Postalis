package sad.ami.postalis.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import sad.ami.postalis.client.screen.base.IPostalisScreen;

public class ChecklistAbilityScreen extends Screen implements IPostalisScreen {
    public ChecklistAbilityScreen() {
        super(Component.literal("checklist_ability_screen"));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

    }
}
