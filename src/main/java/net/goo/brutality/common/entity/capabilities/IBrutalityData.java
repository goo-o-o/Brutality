package net.goo.brutality.common.entity.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

import java.util.function.Predicate;

public interface IBrutalityData {
    CompoundTag serializeNBT();
    void deserializeNBT(CompoundTag nbt);
    default Predicate<Entity> predicate() {
        return e -> true;
    }
}