package sad.ami.postalis.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import sad.ami.postalis.api.system.geo.util.IGeoObject;

import java.util.List;

public class OrnamentGlove extends Item implements IGeoObject {
    public OrnamentGlove() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.empty());

        tooltipComponents.add(Component.translatable("tooltip.postalis.ornament_glove").withStyle(ChatFormatting.GRAY));
    }
}
