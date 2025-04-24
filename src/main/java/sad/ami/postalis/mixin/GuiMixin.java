package sad.ami.postalis.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sad.ami.postalis.client.screen.ChecklistAbilityScreen;

@Mixin(Gui.class)
public class GuiMixin {
    @Final
    private Minecraft minecraft;

    @Shadow
    private int toolHighlightTimer;

    @Shadow
    private ItemStack lastToolHighlight;

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void onRenderCrosshair(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (!ChecklistAbilityScreen.isChecklistScreen())
            return;

        ci.cancel();
    }

    @Inject(method = "renderSelectedItemName(Lnet/minecraft/client/gui/GuiGraphics;I)V", at = @At("HEAD"), cancellable = true)
    private void postalis$renderIconNextToText(GuiGraphics guiGraphics, int yShift, CallbackInfo ci) {
        if (ChecklistAbilityScreen.isChecklistScreen())
            ci.cancel();
        else {
            if (toolHighlightTimer <= 0 || lastToolHighlight.isEmpty() || minecraft.gameMode == null)
                return;

            var hud = (Gui) (Object) this;
            var mutablecomponent = Component.empty().append(lastToolHighlight.getHoverName()).withStyle(lastToolHighlight.getRarity().getStyleModifier());

            if (lastToolHighlight.has(DataComponents.CUSTOM_NAME))
                mutablecomponent.withStyle(ChatFormatting.ITALIC);

            int k = guiGraphics.guiHeight() - Math.max(yShift, 59);

            if (!minecraft.gameMode.canHurtPlayer())
                k += 14;

            var alpha = (int) (toolHighlightTimer * 256.0F / 10.0F);

            if (alpha > 255)
                alpha = 255;

            if (alpha > 0) {
                var texture = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/item/diamond_sword.png"); // for example
                var iconX = (guiGraphics.guiWidth() - hud.getFont().width(lastToolHighlight.getHighlightTip(mutablecomponent))) / 2 - 20;

                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, texture);

                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(0, 0, 100);

                guiGraphics.setColor(1.0F, 1.0F, 1.0F, alpha / 255.0F);
                guiGraphics.blit(texture, iconX, k, 0, 0, 16, 16, 16, 16);
                guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

                guiGraphics.pose().popPose();

                RenderSystem.disableBlend();
            }
        }
    }
}
