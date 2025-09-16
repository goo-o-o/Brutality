package net.goo.brutality.item.curios.vanity;

import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.item.Rarity;

import java.util.List;

public class SolarSystem extends BrutalityCurioItem {

    public SolarSystem(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.HEAD;
    }

    @Override
    public boolean followBodyRotations() {
        return false;
    }

    @Override
    public boolean translateIfSneaking() {
        return false;
    }
}
