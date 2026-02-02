package net.goo.brutality.common.registry;

import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.util.attribute.AttributeCalculationHelper;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.List;

public class PlunderChest extends BrutalityCurioItem {

    public PlunderChest(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public float onWearerMeleeHit(LivingEntity attacker, ItemStack weapon, ItemStack curio, Entity victim, DamageSource source, float amount) {
        if (attacker instanceof Player playerAttacker && victim instanceof LivingEntity livingVictim) {
            if (!playerAttacker.getCooldowns().isOnCooldown(curio.getItem())) {
                if (AttributeCalculationHelper.Luck.roll(attacker, 0.1F, 0.01F)) {
                    List<MobEffectInstance> stealable = livingVictim.getActiveEffects().stream()
                            .filter(e -> e.getEffect().isBeneficial())
                            .toList();

                    if (!stealable.isEmpty()) {
                        MobEffectInstance target = stealable.get(attacker.getRandom().nextInt(stealable.size()));

                        // Perform the swap
                        livingVictim.removeEffect(target.getEffect());
                        attacker.addEffect(new MobEffectInstance(target)); // Clone the effect

                        // Visuals & Cooldown
                        attacker.playSound(BrutalitySounds.TREASURE_CHEST_LOCK.get(), 1.5F, 1.0F);
                        playerAttacker.getCooldowns().addCooldown(curio.getItem(), 100);
                    }
                }
            }
        }
        return super.onWearerMeleeHit(attacker, weapon, curio, victim, source, amount);
    }
}
