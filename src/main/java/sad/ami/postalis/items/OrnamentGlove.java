package sad.ami.postalis.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import sad.ami.postalis.api.system.geo.manage.IGeoRendererManager;
import sad.ami.postalis.api.system.geo.samples.ResourceAssetsSample;
import sad.ami.postalis.client.renderer.item.OrnamentGloveRenderer;

import java.util.List;

public class OrnamentGlove extends Item implements IGeoRendererManager {
    public OrnamentGlove() {
        super(new Properties().stacksTo(1));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public IClientItemExtensions getCustomRender() {
        return new OrnamentGloveRenderer(new ResourceAssetsSample(this));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.empty());

        tooltipComponents.add(Component.translatable("tooltip.postalis.ornament_glove").withStyle(ChatFormatting.GRAY));
    }
}
