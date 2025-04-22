package sad.ami.postalis.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import sad.ami.postalis.client.screen.base.IPostalisScreen;
import sad.ami.postalis.items.base.ISwordItem;

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

    public static boolean isChecklistScreen() {
        var mc = Minecraft.getInstance();

        if (mc.player == null)
            return false;

        return mc.player.getMainHandItem().getItem() instanceof ISwordItem && mc.screen instanceof ChecklistAbilityScreen;
    }
}
