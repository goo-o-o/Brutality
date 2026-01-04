package net.goo.brutality.mob_effect.gastronomy;

import net.minecraft.world.entity.LivingEntity;

public interface IGastronomyEffect {

    enum Type {
        WET,
        DRY,
        BOTH
    }

    Type getType();

    boolean scalesWithLevel();

    boolean modifiesDamage();

    float baseMultiplier();

    float multiplierPerLevel();

    default void applyEffect(LivingEntity attacker, LivingEntity victim, int level) {}
}
