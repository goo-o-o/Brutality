package net.goo.brutality.entity.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class CapabilityProvider<T extends INBTSerializable<CompoundTag>> implements ICapabilitySerializable<CompoundTag> {
    private final T instance;
    private final LazyOptional<T> optional;
    private final Capability<T> capability;

    public CapabilityProvider(Capability<T> capability, Supplier<T> instanceSupplier) {
        this.capability = capability;
        this.instance = instanceSupplier.get();
        this.optional = LazyOptional.of(() -> this.instance);
    }

    @Override
    public @NotNull <U> LazyOptional<U> getCapability(@NotNull Capability<U> cap, @Nullable Direction side) {
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