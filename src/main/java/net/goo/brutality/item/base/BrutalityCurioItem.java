package net.goo.brutality.item.base;

import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class BrutalityCurioItem extends BrutalityGenericItem implements ICurioItem {

    public BrutalityCurioItem(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    public boolean followBodyRotations() {
        return true;
    }
    public boolean followHeadRotations() {
        return true;
    }

    /**
     * Only works if the bone is within the chest slot, this is intentional
     */
    public boolean rotateIfSneaking() {
        return true;
    }
    public boolean translateIfSneaking() {
        return true;
    }
}
