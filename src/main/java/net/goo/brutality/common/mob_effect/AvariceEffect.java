package net.goo.brutality.common.mob_effect;

import net.goo.brutality.common.registry.BrutalityAttributes;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.mcreator.terramity.init.TerramityModParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AvariceEffect extends MobEffect {
    private static final UUID AVARICE_UUID = UUID.fromString("edafe55a-e0b0-44f4-813b-ba533a2787d6");

    public AvariceEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        this.addAttributeModifier(BrutalityAttributes.DAMAGE_TAKEN.get(), String.valueOf(AVARICE_UUID), 0, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public double getAttributeModifierValue(int amplifier, @NotNull AttributeModifier modifier) {
        return (amplifier + 1) * 0.01F;
    }

    public static float handleProc(LivingEntity victim, LivingEntity attacker, float amount) {
        if (attacker.hasEffect(BrutalityEffects.AVARICE.get()) && attacker.getEffect(BrutalityEffects.AVARICE.get()).getAmplifier() >= 99) {
            spawnParticles(victim);
            attacker.removeEffect(BrutalityEffects.AVARICE.get());
            return amount * 10;
        }
        return amount;
    }

    private static void spawnParticles(LivingEntity living) {
        RandomSource random = living.getRandom();
        float x = (float) living.getX(), y = (float) living.getY(0.5), z = (float) living.getZ();
        Level level = living.level();
        for (int i = 0; i < 10; i++) {
            level.addParticle(TerramityModParticleTypes.COIN_PARTICLE.get(), x, y, z,
                    Mth.nextFloat(random, -0.5F, 0.5F),
                    Mth.nextFloat(random, -0.5F, 0.5F),
                    Mth.nextFloat(random, -0.5F, 0.5F)
            );
        }
    }

    public static void handleHurt(LivingEntity living) {
        spawnParticles(living);
        living.removeEffect(BrutalityEffects.AVARICE.get());
    }
}
