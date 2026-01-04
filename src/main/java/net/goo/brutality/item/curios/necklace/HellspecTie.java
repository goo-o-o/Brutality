package net.goo.brutality.item.curios.necklace;

import net.goo.brutality.item.curios.base.BaseNecklaceCurio;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.item.Rarity;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;

public class HellspecTie extends BaseNecklaceCurio {


    public HellspecTie(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

}
