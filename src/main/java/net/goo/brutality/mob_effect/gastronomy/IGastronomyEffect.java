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

    default float multiplierPerLevel() {
        return 0.2F;
    }

    default void applyEffect(LivingEntity attacker, LivingEntity victim, int level) {}
}
