package net.goo.brutality.mixin.accessors;

import net.minecraft.world.entity.LivingEntity;

public interface IBrutalityAttribute {
    void brutality$setOwner(LivingEntity entity);
    LivingEntity brutality$getOwner();
}