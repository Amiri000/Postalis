package sad.ami.postalis.client.screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import sad.ami.postalis.client.screen.base.IPostalisScreen;

public class UpgradeAbilityScreen extends Screen implements IPostalisScreen {
    protected UpgradeAbilityScreen(Component title) {
        super(title);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
