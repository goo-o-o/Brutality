package net.goo.brutality.common.mob_effect;

import net.goo.brutality.common.registry.BrutalityParticles;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TranquilityEffect extends MobEffect {

    public static final UUID ARMOR = UUID.fromString("e7a0da53-bd26-4ce4-b853-6fe5edc0d63d");
    public static final UUID STEALTH = UUID.fromString("218db2a7-b173-4cd2-a615-2524cb2cc989");

    public TranquilityEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(BrutalityAttributes.STEALTH.get(), STEALTH.toString(), 0, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(Attributes.ARMOR, ARMOR.toString(), 0, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {

        if (entity.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(BrutalityParticles.YIN_YANG_PARTICLE.get(),
                    entity.getX(), entity.getY() + entity.getBbHeight() / 2, entity.getZ(), 1,
                    0.5, 0.5, 0.5, 0);


        }

    }


    @Override
    public double getAttributeModifierValue(int amplifier, @NotNull AttributeModifier modifier) {
        UUID modifierId = modifier.getId();
        if (modifierId.equals(STEALTH) || modifierId.equals(ARMOR))
            return (amplifier + 1) * 0.05F;
        return 0;
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 5 == 0;
    }
}
