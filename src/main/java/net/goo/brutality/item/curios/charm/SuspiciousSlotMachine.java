package net.goo.brutality.item.curios.charm;

import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.item.Rarity;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;

public class SuspiciousSlotMachine extends BrutalityCurioItem {

    public SuspiciousSlotMachine(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }
}
