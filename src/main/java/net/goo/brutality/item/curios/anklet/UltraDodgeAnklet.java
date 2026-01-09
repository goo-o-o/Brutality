package net.goo.brutality.item.curios.anklet;

import net.goo.brutality.item.base.BrutalityAnkletItem;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.List;

public class UltraDodgeAnklet extends BrutalityAnkletItem {


    public UltraDodgeAnklet(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }


    @Override
    public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
        if (dodger instanceof Player wearer) {
            if (!wearer.getCooldowns().isOnCooldown(this)) {
                wearer.addEffect(new MobEffectInstance(BrutalityModMobEffects.ULTRA_DODGE.get(), 100));
                wearer.getCooldowns().addCooldown(this, 100);
            }
        }
    }
}
