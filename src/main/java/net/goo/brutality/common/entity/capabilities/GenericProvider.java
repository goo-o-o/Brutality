package net.goo.brutality.common.entity.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class GenericProvider<T extends IBrutalityData> implements ICapabilitySerializable<CompoundTag> {
    private final T instance;
    private final LazyOptional<T> optional;
    private final Capability<T> capability;

    public GenericProvider(Capability<T> cap, T instance) {
        this.capability = cap;
        this.instance = instance;
        this.optional = LazyOptional.of(() -> this.instance);
    }

    @Override
    public <X> @NotNull LazyOptional<X> getCapability(@NotNull Capability<X> cap, Direction side) {
        return capability.orEmpty(cap, optional);
    }

    @Override
    public CompoundTag serializeNBT() {
        return instance.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        instance.deserializeNBT(nbt);
    }
}