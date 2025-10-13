package net.goo.brutality.item.weapon.throwing;

import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.List;

public class PouchOPhotons extends Photon {


    public PouchOPhotons(int pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    @Override
    public void throwProjectile(ItemStack stack, Player player) {
        for (int i = 0; i < player.getRandom().nextIntBetweenInclusive(3, 10); i++) {
            super.throwProjectile(stack, player);
        }
    }

    @Override
    public float getThrowInaccuracy() {
        return 3;
    }

    @Override
    public float getInitialThrowVelocity() {
        return super.getInitialThrowVelocity() * 1.5F;
    }
}
