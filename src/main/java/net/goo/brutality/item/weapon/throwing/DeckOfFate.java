package net.goo.brutality.item.weapon.throwing;

import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityThrowingItem;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Rarity;

import java.util.List;

public class DeckOfFate extends BrutalityThrowingItem {


    public DeckOfFate(int pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories.AttackType getAttackType() {
        return BrutalityCategories.AttackType.BLUNT;
    }

    @Override
    public EntityType<? extends Projectile> getThrownEntity() {

        return BrutalityModEntities.FATE_CARD.get();
    }

    @Override
    public float getInitialThrowVelocity() {
        return 1F;
    }
}
