package sad.ami.postalis.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sad.ami.postalis.client.screen.ChecklistAbilityScreen;
import sad.ami.postalis.client.screen.base.IPostalisScreen;
import sad.ami.postalis.items.base.ISwordItem;

@Mixin(Gui.class)
public class GuiMixin {
    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void onRenderCrosshair(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (!ChecklistAbilityScreen.isChecklistScreen())
            return;

        ci.cancel();
    }
}
