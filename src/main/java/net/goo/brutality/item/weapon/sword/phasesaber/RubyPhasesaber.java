package net.goo.brutality.item.weapon.sword.phasesaber;

import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;

import java.util.List;

public class RubyPhasesaber extends BasePhasesaber {
    public RubyPhasesaber(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);

    }
}
