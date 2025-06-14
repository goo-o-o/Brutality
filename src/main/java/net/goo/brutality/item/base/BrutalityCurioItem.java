package net.goo.brutality.item.base;

import net.goo.brutality.item.BrutalityItemCategories;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class BrutalityCurioItem extends BrutalityGenericItem implements ICurioItem {
    public BrutalityCurioItem(Properties pProperties, String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(new Item.Properties().stacksTo(1), identifier, rarity, descriptionComponents);
    }

    @Override
    public BrutalityItemCategories getCategory() {
        return BrutalityItemCategories.CURIO;
    }

}
