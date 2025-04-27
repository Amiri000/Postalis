package sad.ami.postalis.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.config.PostalisConfig;
import sad.ami.postalis.handlers.HotkeyHandlers;
import sad.ami.postalis.handlers.OpenChecklistScreenHandler;
import sad.ami.postalis.utils.PlayerUtils;

@Mixin(Gui.class)
public abstract class GuiMixin {
    @Final
    @Shadow
    private Minecraft minecraft;

    @Shadow
    private int toolHighlightTimer;

    @Shadow
    private ItemStack lastToolHighlight;

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void onRenderCrosshair(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (PlayerUtils.isChecklistScreen()) {
            ci.cancel();

            return;
        }

        var player = minecraft.player;

        if (player == null || guiGraphics == null || !PlayerUtils.inMainHandPostalisSword(player) || !PostalisConfig.isHintVisible())
            return;

        var font = minecraft.font;
        var text = Component.translatable("warning.postalis.screen", HotkeyHandlers.CHECKLIST_MENU.getTranslatedKeyMessage().getString());

        guiGraphics.drawString(font, text, (guiGraphics.guiWidth() / 2) - font.width(text) / 2, ((guiGraphics.guiHeight() / 2) - font.lineHeight / 2) + 13, 0xFFFFFFFF, true);
    }

    @Inject(method = "renderSelectedItemName(Lnet/minecraft/client/gui/GuiGraphics;I)V", at = @At("HEAD"), cancellable = true)
    private void postalis$renderIconNextToText(GuiGraphics guiGraphics, int yShift, CallbackInfo ci) {
        if (PlayerUtils.isChecklistScreen()) {
            ci.cancel();

            return;
        }

        var holdTime = OpenChecklistScreenHandler.holdTime;
        var player = minecraft.player;

        if (player == null || !PlayerUtils.inMainHandPostalisSword(player))
            return;

        if (holdTime > 0)
            toolHighlightTimer = 20;

        if (toolHighlightTimer <= 0 || lastToolHighlight.isEmpty() || minecraft.gameMode == null || !lastToolHighlight.is(player.getMainHandItem().getItem()))
            return;

        var hud = (Gui) (Object) this;
        var mutablecomponent = Component.empty().append(lastToolHighlight.getHoverName()).withStyle(lastToolHighlight.getRarity().getStyleModifier()).withStyle(ChatFormatting.ITALIC);

        var k = (guiGraphics.guiHeight() - Math.max(yShift, 59)) + (minecraft.gameMode.canHurtPlayer() ? 0 : 14);
        var alpha = Math.min(255, (int) (toolHighlightTimer * 256.0F / 10.0F));

        var texture = ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "textures/gui/selected_item_textures.png");
        var textWidth = hud.getFont().width(lastToolHighlight.getHighlightTip(mutablecomponent));
        var centerX = guiGraphics.guiWidth() / 2;

        var progress = Math.min(1.0f, holdTime / 20f);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 100);

        guiGraphics.setColor(Mth.lerp(progress, 1.0f, 0.1f), Mth.lerp(progress, 1.0f, 0.2f), Mth.lerp(progress, 1.0f, 1.0f), alpha / 255.0F);
        guiGraphics.blit(texture, centerX - textWidth / 2 - 58, k - 2, 1, 0, 55, 10, 130, 10);
        guiGraphics.blit(texture, centerX + textWidth / 2 + 2, k - 2, 75, 0, 55, 10, 130, 10);

        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.pose().popPose();
        RenderSystem.disableBlend();
    }
}
