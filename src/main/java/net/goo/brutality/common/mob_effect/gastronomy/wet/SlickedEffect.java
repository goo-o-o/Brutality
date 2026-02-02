package net.goo.brutality.common.mob_effect.gastronomy.wet;

import net.goo.brutality.common.mob_effect.gastronomy.IGastronomyEffect;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SlickedEffect extends MobEffect implements IGastronomyEffect {

    UUID MOVE_SPEED = UUID.fromString("9b5dd583-f667-4fac-a147-05756a8559fd");
    UUID JUMP_POWER = UUID.fromString("76c463f9-1e8d-448f-ba4f-dee194ec2d9c");

    public SlickedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, String.valueOf(MOVE_SPEED), -0.5, AttributeModifier.Operation.MULTIPLY_BASE);
    }

    @Override
    public double getAttributeModifierValue(int amplifier, @NotNull AttributeModifier modifier) {
        UUID uuid = modifier.getId();
        if (uuid.equals(JUMP_POWER)) {
            return -0.2 * (1 + amplifier);
        }
        return super.getAttributeModifierValue(amplifier, modifier);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        super.applyEffectTick(pLivingEntity, pAmplifier);

        if (pLivingEntity.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(BrutalityParticles.SLICKED_PARTICLE.get(),
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
        return 0.15F;
    }
}

