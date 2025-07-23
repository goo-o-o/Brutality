package net.goo.brutality.item.curios.vanity;

import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.item.Rarity;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;

public class WoolyBlindfold extends BrutalityCurioItem {

    public WoolyBlindfold(Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.HEAD;
    }
}
