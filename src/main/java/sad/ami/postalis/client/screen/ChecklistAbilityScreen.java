package sad.ami.postalis.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import sad.ami.postalis.client.ClientPlayerHandlers;
import sad.ami.postalis.client.screen.base.BaseScreen;

public class ChecklistAbilityScreen extends BaseScreen {
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

    @Override
    public void onClose() {
        super.onClose();

        Minecraft.getInstance().options.setCameraType(ClientPlayerHandlers.oldCameraType);
    }
}
