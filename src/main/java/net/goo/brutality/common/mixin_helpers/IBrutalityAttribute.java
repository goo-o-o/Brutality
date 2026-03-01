package net.goo.brutality.common.mixin_helpers;

import net.minecraft.world.entity.LivingEntity;

public interface IBrutalityAttribute {
    void brutality$setOwner(LivingEntity entity);
    LivingEntity brutality$getOwner();
}