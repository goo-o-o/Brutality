package net.goo.brutality.common.mob_effect.gastronomy;

import net.goo.brutality.common.registry.BrutalityAttributes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Supplier;

public class OiledEffect extends GastronomyParticleEffect {
    private static final UUID FRICTION = UUID.fromString("1639bfba-4f30-471f-8174-0eac3e50b3c5");
    private static final UUID JUMP_HEIGHT = UUID.fromString("838770ce-598c-452e-83e0-22d19464df5a");

    public OiledEffect(MobEffectCategory pCategory, Type type, float baseMult, float perLevelMult, Supplier<? extends ParticleOptions> particleSupplier) {
        super(pCategory, type, baseMult, perLevelMult, particleSupplier);
        this.addAttributeModifier(BrutalityAttributes.GROUND_FRICTION.get(), String.valueOf(FRICTION), 0, AttributeModifier.Operation.MULTIPLY_BASE);
        this.addAttributeModifier(BrutalityAttributes.JUMP_HEIGHT.get(), String.valueOf(JUMP_HEIGHT), 0, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }


    @Override
    public double getAttributeModifierValue(int amplifier, @NotNull AttributeModifier modifier) {
        return -0.1 * (1 + amplifier);
    }

}

