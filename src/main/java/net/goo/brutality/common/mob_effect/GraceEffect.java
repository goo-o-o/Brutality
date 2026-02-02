package net.goo.brutality.common.mob_effect;

import net.mcreator.terramity.init.TerramityModParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class GraceEffect extends MobEffect {

    public GraceEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(TerramityModParticleTypes.HOLY_GLINT.get(), entity.getX(), entity.getY(0.5), entity.getZ(), 2, 1,1,1, 0);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 4 == 0;
    }


}
