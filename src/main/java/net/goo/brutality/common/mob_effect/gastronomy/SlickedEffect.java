package net.goo.brutality.common.mob_effect.gastronomy;

import net.goo.brutality.common.registry.BrutalityAttributes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Supplier;

public class SlickedEffect extends GastronomyParticleEffect {

    private static final UUID MOVE_SPEED = UUID.fromString("9b5dd583-f667-4fac-a147-05756a8559fd");
    private static final UUID JUMP_POWER = UUID.fromString("76c463f9-1e8d-448f-ba4f-dee194ec2d9c");

    public SlickedEffect(MobEffectCategory pCategory, Type type, float baseMult, float perLevelMult, Supplier<? extends ParticleOptions> particleSupplier) {
        super(pCategory, type, baseMult, perLevelMult, particleSupplier);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, String.valueOf(MOVE_SPEED), -0.5, AttributeModifier.Operation.MULTIPLY_BASE);
        this.addAttributeModifier(BrutalityAttributes.JUMP_HEIGHT.get(), String.valueOf(JUMP_POWER), 0, AttributeModifier.Operation.MULTIPLY_BASE);
    }


    @Override
    public double getAttributeModifierValue(int amplifier, @NotNull AttributeModifier modifier) {
        UUID uuid = modifier.getId();
        if (uuid.equals(JUMP_POWER)) {
            return -0.2 * (1 + amplifier);
        }
        return super.getAttributeModifierValue(amplifier, modifier);
    }

}

