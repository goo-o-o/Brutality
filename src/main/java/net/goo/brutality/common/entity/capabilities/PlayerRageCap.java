package net.goo.brutality.common.entity.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.function.Predicate;

@AutoRegisterCapability
public class PlayerRageCap implements IBrutalityData {
    private float currentRage = 0;
    public static final String RAGE_KEY = "rage";

    public float getRage() {
        return this.currentRage;
    }

    public void setRage(float amount) {
        this.currentRage = Math.max(0, amount);
    }

    public void modifyRageValue(float amount) {
        setRage(getRage() + amount);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat(RAGE_KEY, this.currentRage);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.currentRage = nbt.getFloat(RAGE_KEY);
    }

    @Override
    public Predicate<Entity> predicate() {
        return e -> e instanceof Player;
    }
}