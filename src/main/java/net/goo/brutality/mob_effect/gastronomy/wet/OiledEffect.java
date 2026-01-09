package net.goo.brutality.mob_effect.gastronomy.wet;

import net.goo.brutality.mob_effect.gastronomy.IGastronomyEffect;
import net.goo.brutality.registry.BrutalityModAttributes;
import net.goo.brutality.registry.BrutalityModParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class OiledEffect extends MobEffect implements IGastronomyEffect {
    UUID FRICTION = UUID.fromString("1639bfba-4f30-471f-8174-0eac3e50b3c5");
    UUID JUMP_HEIGHT = UUID.fromString("838770ce-598c-452e-83e0-22d19464df5a");

    public OiledEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(BrutalityModAttributes.GROUND_FRICTION.get(), String.valueOf(FRICTION), 0, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(BrutalityModAttributes.JUMP_HEIGHT.get(), String.valueOf(JUMP_HEIGHT), 0, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public double getAttributeModifierValue(int amplifier, @NotNull AttributeModifier modifier) {
        return -0.1 * (1 + amplifier);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        super.applyEffectTick(pLivingEntity, pAmplifier);

        if (pLivingEntity.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(BrutalityModParticles.OILED_PARTICLE.get(),
                    pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntity.getBbHeight() / 2, pLivingEntity.getZ(), 1,
                    0.5, 0.5, 0.5
                    , 0);

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
        return false;
    }

    @Override
    public boolean modifiesDamage() {
        return false;
    }

    @Override
    public float baseMultiplier() {
        return 0.10F;
    }

    @Override
    public float multiplierPerLevel() {
        return 0;
    }

}

