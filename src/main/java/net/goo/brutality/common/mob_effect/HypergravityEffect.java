package net.goo.brutality.common.mob_effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class HypergravityEffect extends MobEffect {
    public static UUID HYPERGRAVITY_GRAVITY_UUID = UUID.fromString("8215f131-77dd-4f71-9f60-89524767bfe1");
    public static UUID HYPERGRAVITY_MS_UUID = UUID.fromString("c1a36f9e-5302-486f-b1b1-3c52f59e13b7");
    protected final double multiplier;

    public HypergravityEffect(MobEffectCategory category, int color, double multiplier) {
        super(category, color);
        this.multiplier = multiplier;
        this.addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), String.valueOf(HYPERGRAVITY_GRAVITY_UUID), 0, AttributeModifier.Operation.MULTIPLY_BASE);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, String.valueOf(HYPERGRAVITY_MS_UUID), 0, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public double getAttributeModifierValue(int amplifier, @NotNull AttributeModifier modifier) {
        UUID modifierId = modifier.getId();
        if (modifierId.equals(HYPERGRAVITY_GRAVITY_UUID)) {
            return this.multiplier * (amplifier + 1);
        } else if (modifierId.equals(HYPERGRAVITY_MS_UUID)) {
            return Math.min(this.multiplier * (amplifier + 1) * -0.5F, 0.9);
        }
        return 0;
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity.tickCount % 10 == 0) {
            pLivingEntity.invulnerableTime = 0;
            pLivingEntity.hurt(pLivingEntity.damageSources().flyIntoWall(), (pAmplifier - 5) * 0.5F);
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pAmplifier > 5;
    }
}
