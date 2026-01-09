package net.goo.brutality.item.weapon.throwing;

import net.goo.brutality.item.base.BrutalityThrowingItem;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.List;
import java.util.function.Supplier;

public class PouchOPhotons extends BrutalityThrowingItem {


    public PouchOPhotons(int pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents, Supplier<? extends EntityType<? extends Projectile>> entityTypeSupplier) {
        super(pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents, entityTypeSupplier);
    }

    @Override
    public void handleThrowPacket(ItemStack stack, Player player) {
        for (int i = 0; i < player.getRandom().nextIntBetweenInclusive(3, 10); i++) {
            super.handleThrowPacket(stack, player);
        }
    }

}
