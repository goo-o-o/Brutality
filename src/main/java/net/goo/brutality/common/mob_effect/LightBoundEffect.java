package net.goo.brutality.common.mob_effect;

import net.mcreator.terramity.init.TerramityModParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class LightBoundEffect extends MobEffect {
    UUID LIGHT_BOUND_MS_UUID = UUID.fromString("02a2a4a2-d957-464f-8a42-bc930b9aa754");

    public LightBoundEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, String.valueOf(LIGHT_BOUND_MS_UUID), -10, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(TerramityModParticleTypes.HOLY_GLINT.get(), entity.getX(), entity.getY(0.5), entity.getZ(), 2, 1, 1, 1, 0);
        }
    }


    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 4 == 0;
    }
}
