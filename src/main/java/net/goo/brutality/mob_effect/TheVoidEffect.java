package net.goo.brutality.mob_effect;

import net.goo.brutality.registry.BrutalityModParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class TheVoidEffect extends MobEffect {

    public TheVoidEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(BrutalityModParticles.BLACK_HOLE_PARTICLE.get(),
                    entity.getX(), entity.getY() + entity.getBbHeight() / 2, entity.getZ(), 1,
                    0.5, 0.5, 0.5
                    , 0);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 5 == 0;
    }
}
