package net.goo.brutality.entity.capabilities;

import net.goo.brutality.registry.BrutalityCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityStarCountCapProvider implements ICapabilitySerializable<CompoundTag> {
    private final EntityCapabilities.EntityStarCountCap instance;
    private final LazyOptional<EntityCapabilities.EntityStarCountCap> optional;

    public EntityStarCountCapProvider() {
        this(new EntityCapabilities.EntityStarCountCap());
    }

    public EntityStarCountCapProvider(EntityCapabilities.EntityStarCountCap instance) {
        this.instance = instance;
        this.optional = LazyOptional.of(() -> this.instance);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return BrutalityCapabilities.ENTITY_STAR_COUNT_CAP.orEmpty(cap, optional);
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