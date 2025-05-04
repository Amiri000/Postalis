package sad.ami.postalis;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sad.ami.postalis.api.PlayerItemInteraction;
import sad.ami.postalis.config.PostalisConfig;
import sad.ami.postalis.init.*;
import sad.ami.postalis.utils.PlayerUtils;

@Mod(Postalis.MODID)
public class Postalis {
    public static final String MODID = "postalis";

    public Postalis(IEventBus modEventBus, ModContainer modContainer) {
        CreativeTabRegistry.CREATIVE_TAB.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        EntityRegistry.ENTITIES.register(modEventBus);
        PDataComponentRegistry.DATA_COMPONENTS.register(modEventBus);

        ItemRegistry.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, PostalisConfig.CLIENT_SPEC);
    }

    public static void fgfg(ItemStack stack, ItemDisplayContext context, boolean leftHanded, PoseStack poseStack, MultiBufferSource buffer, CallbackInfo ci) {
        var mc = Minecraft.getInstance();

        if (mc.player == null || stack.getItem() != ItemRegistry.WIND_BREAKER.get() || context == ItemDisplayContext.GUI || !PlayerUtils.isSameUUID(stack, mc.player.getUUID())
                || context == ItemDisplayContext.GROUND || context == ItemDisplayContext.FIXED || context == ItemDisplayContext.HEAD)
            return;

        float time = PlayerItemInteraction.useTickCount + mc.getTimer().getGameTimeDeltaPartialTick(false);
        float amplitude = Math.min(((float) PlayerItemInteraction.useTickCount / 20) * 0.04F, 0.5f);
        float frequency = 0.25f;

        poseStack.translate(Math.sin(time * 2 * Math.PI * frequency) * amplitude, Math.cos(time * 2 * Math.PI * frequency * 0.5) * amplitude * 0.5f, 0);
    }
}
