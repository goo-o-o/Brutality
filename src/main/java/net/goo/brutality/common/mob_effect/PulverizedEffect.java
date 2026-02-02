package net.goo.brutality.common.mob_effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class PulverizedEffect extends MobEffect {


    public PulverizedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {

        pLivingEntity.invulnerableTime = 0;
        if (pLivingEntity.getLastAttacker() instanceof Player attacker) {
            pLivingEntity.hurt(pLivingEntity.damageSources().playerAttack(attacker), pAmplifier);
        } else {
            pLivingEntity.hurt(pLivingEntity.damageSources().mobAttack(pLivingEntity.getLastAttacker()), pAmplifier);
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
