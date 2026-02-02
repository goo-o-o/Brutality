package net.goo.brutality.common.mob_effect.gastronomy.dry;

import net.goo.brutality.common.mob_effect.gastronomy.IGastronomyEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class CandiedEffect extends MobEffect implements IGastronomyEffect {
    UUID CANDIED_ARMOR_UUID = UUID.fromString("2a0eeca1-5f47-4888-9f74-de3640374003");

    public CandiedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        this.addAttributeModifier(Attributes.ARMOR, String.valueOf(CANDIED_ARMOR_UUID), -0.15, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        super.applyEffectTick(pLivingEntity, pAmplifier);

//        if (pLivingEntity.spellLevel() instanceof ServerLevel serverLevel) {
//            serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE,
//                    pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntity.getBbHeight() / 2, pLivingEntity.getZ(), 1,
//                    0.5, 0.5, 0.5
//                    ,0);
//
//        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration % 4 == 0;
    }

    @Override
    public Type getType() {
        return Type.DRY;
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

    @Override
    public float multiplierPerLevel() {
        return 0.1F;
    }
}

