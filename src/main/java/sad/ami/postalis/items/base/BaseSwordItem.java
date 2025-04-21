package sad.ami.postalis.items.base;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;

public class BaseSwordItem extends SwordItem implements ISwordItem, PostalisItem {
    public BaseSwordItem() {
        super(Tiers.DIAMOND, new Item.Properties().rarity(Rarity.RARE).stacksTo(1));
    }
}
