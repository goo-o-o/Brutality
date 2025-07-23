package net.goo.brutality.item.base;

import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class BrutalityCurioItem extends BrutalityGenericItem implements ICurioItem {

    public BrutalityCurioItem(Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    public boolean followBodyRotations() {
        return true;
    }
}
