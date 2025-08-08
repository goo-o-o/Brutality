package net.goo.brutality.mob_effect.gastronomy.wet;

import net.goo.brutality.mob_effect.gastronomy.IGastronomyEffect;
import net.goo.brutality.registry.BrutalityModParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class SlickedEffect extends MobEffect implements IGastronomyEffect {

    public SlickedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);

        UUID SLICKED_MS_UUID = UUID.fromString("9b5dd583-f667-4fac-a147-05756a8559fd");
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, String.valueOf(SLICKED_MS_UUID), -0.5, AttributeModifier.Operation.MULTIPLY_BASE);

    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        super.applyEffectTick(pLivingEntity, pAmplifier);

        if (pLivingEntity.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(BrutalityModParticles.SLICKED_PARTICLE.get(),
                    pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntity.getBbHeight() / 2, pLivingEntity.getZ(), 1,
                    0.5, 0.5, 0.5
                    ,0);

        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration % 4 == 0;
    }

    @Override
    public IGastronomyEffect.Type getType() {
        return IGastronomyEffect.Type.WET;
    }

    @Override
    public boolean scalesWithLevel() {
        return true;
    }

    @Override
    public boolean modifiesDamage() {
        return true;
    }

    @Override
    public float baseMultiplier() {
        return 0.05F;
    }


}

