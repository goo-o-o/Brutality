package net.goo.brutality.mob_effect;

import net.goo.brutality.item.weapon.custom.AtomicJudgementHammer;
import net.goo.brutality.registry.ModParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class RadiationEffect extends MobEffect {
    public RadiationEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        Level level = pLivingEntity.level();

        if (!level.isClientSide()) {
            if (!(pLivingEntity.getMainHandItem().getItem() instanceof AtomicJudgementHammer || pLivingEntity.getOffhandItem().getItem() instanceof AtomicJudgementHammer))  {
                pLivingEntity.hurt(pLivingEntity.damageSources().magic(), 1.0F);
                ((ServerLevel) level).sendParticles(ModParticles.RADIATION_PARTICLE.get(), pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntity.getBbHeight() / 2, pLivingEntity.getZ(), 5, 0.5, 0.5 ,0.5, 0);

            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        int divisor = Math.max(1, 20 / Math.max(1, Math.min(pAmplifier + 1, 255))); // Ensure divisor is at least 1
        return pDuration % divisor == 0;
    }
}
