package net.goo.brutality.item.weapon.sword;

import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalitySwordItem;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;

import java.util.List;

public class WorldTreeSword extends BrutalitySwordItem {
    public WorldTreeSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories.AttackType getAttackType() {
        return BrutalityCategories.AttackType.BLUNT;
    }
}
