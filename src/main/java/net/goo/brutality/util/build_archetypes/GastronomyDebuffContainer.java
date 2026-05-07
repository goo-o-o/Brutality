package net.goo.brutality.util.build_archetypes;

import net.minecraft.world.effect.MobEffect;

import java.util.function.Supplier;

public record GastronomyDebuffContainer(boolean requiresMelee, Supplier<MobEffect> effect, int duration, int levels) {
}