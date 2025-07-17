package net.goo.brutality.item.curios;

import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.item.Rarity;

import java.util.List;

public class OliveOilCharm extends BrutalityCurioItem {

    public OliveOilCharm(String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(identifier, rarity, descriptionComponents);
    }
}
