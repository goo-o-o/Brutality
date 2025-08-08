package net.goo.brutality.entity.capabilities;

import net.goo.brutality.registry.BrutalityCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerComboCapProvider implements ICapabilitySerializable<CompoundTag> {
    private final EntityCapabilities.PlayerComboCap instance;
    private final LazyOptional<EntityCapabilities.PlayerComboCap> optional;

    public PlayerComboCapProvider() {
        this(new EntityCapabilities.PlayerComboCap());
    }

    public PlayerComboCapProvider(EntityCapabilities.PlayerComboCap instance) {
        this.instance = instance;
        this.optional = LazyOptional.of(() -> this.instance);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return BrutalityCapabilities.PLAYER_COMBO_CAP.orEmpty(cap, optional);
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

