package net.goo.brutality.mob_effect;

import net.goo.brutality.registry.BrutalityModParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class EnragedEffect extends MobEffect {

    public static final UUID ENRAGED_AD_UUID = UUID.fromString("e66b00e4-25b0-42df-bbc2-ba95445c2bf5");
    public static final UUID ENRAGED_ARMOR_UUID = UUID.fromString("18918f9b-66f4-4190-8a2d-f8cc9c884ffb");
    public static final UUID ENRAGED_AS_UUID = UUID.fromString("45783c22-2ed9-47fe-a46e-07f6fb8d0d37");
    public static final UUID ENRAGED_MS_UUID = UUID.fromString("4e0f449b-3c0f-445b-b894-6b3dec2c4b03");

    public EnragedEffect(MobEffectCategory category, int color) {
        super(category, color);
        // movement speed (20% + 10% per level)
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, ENRAGED_MS_UUID.toString(), 0, AttributeModifier.Operation.MULTIPLY_TOTAL);
        // attack speed (20% + 10% per level)
        this.addAttributeModifier(Attributes.ATTACK_SPEED, ENRAGED_AS_UUID.toString(), 0, AttributeModifier.Operation.MULTIPLY_TOTAL);
        // attack damage (50% + 5% per level)
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, ENRAGED_AD_UUID.toString(), 0, AttributeModifier.Operation.MULTIPLY_TOTAL);
        // armor (capped at 0, starts at -100% + 10% per level)
        this.addAttributeModifier(Attributes.ARMOR, ENRAGED_ARMOR_UUID.toString(), 0, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {

        if (entity.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(BrutalityModParticles.ENRAGED_PARTICLE.get(),
                    entity.getX(), entity.getY() + entity.getBbHeight() / 2, entity.getZ(), 1,
                    0.5, 0.5, 0.5
                    , 0);


        }


    }


    @Override
    public double getAttributeModifierValue(int amplifier, @NotNull AttributeModifier modifier) {
        UUID modifierId = modifier.getId();
        if (modifierId.equals(ENRAGED_MS_UUID)) {
            return 0.2 + (0.1 * amplifier); // Movement speed: 20% + 10% per level
        } else if (modifierId.equals(ENRAGED_AS_UUID)) {
            return 0.2 + (0.1 * amplifier); // Attack speed: 20% + 10% per level
        } else if (modifierId.equals(ENRAGED_AD_UUID)) {
            return 0.5 + (0.05 * amplifier); // Attack damage: 50% + 5% per level
        } else if (modifierId.equals(ENRAGED_ARMOR_UUID)) {
            return Math.min(-1.0 + (0.1 * amplifier), 0); // Armor: -100% + 10% per level, capped at 0
        }
        return 0; // Fallback for unknown modifiers
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 5 == 0;
    }
}
