package net.goo.brutality.item.weapon.throwing;

import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityThrowingItem;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Rarity;

import java.util.List;

public class CosmicNinjaStar extends BrutalityThrowingItem {


    public CosmicNinjaStar(int pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories.AttackType getAttackType() {
        return BrutalityCategories.AttackType.PIERCE;
    }

    @Override
    public EntityType<? extends Projectile> getThrownEntity() {
        return BrutalityModEntities.STAR_ENTITY.get();
    }
}
