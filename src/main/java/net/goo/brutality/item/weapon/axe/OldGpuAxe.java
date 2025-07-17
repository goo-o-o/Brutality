package net.goo.brutality.item.weapon.custom;

import net.goo.brutality.item.base.BrutalityAxeItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;

public class OldGpuAxe extends BrutalityAxeItem {
    public OldGpuAxe(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, identifier, rarity, descriptionComponents);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }
}
