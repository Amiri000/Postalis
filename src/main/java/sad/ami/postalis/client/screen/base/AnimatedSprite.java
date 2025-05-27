package sad.ami.postalis.client.screen.base;

import lombok.AllArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

@AllArgsConstructor
public class AnimatedSprite {
    private final ResourceLocation texture;
    private final int frameWidth;
    private final int frameHeight;
    private final int totalFrames;
    private final int frameDurationTicks;

    public void render(GuiGraphics guiGraphics, int x, int y) {
        long ticks = Minecraft.getInstance().level.getGameTime();
        int currentFrame = (int) ((ticks / frameDurationTicks) % totalFrames);

        int u = currentFrame * frameWidth;

        guiGraphics.blit(texture, x, y, u, 0, frameWidth, frameHeight, 100, 20);
    }

    public int getWidth() {
        return frameWidth;
    }

    public int getHeight() {
        return frameHeight;
    }
}
