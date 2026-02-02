package net.goo.brutality.common.entity.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.function.Predicate;

@AutoRegisterCapability
public class PlayerBloodCap implements IBrutalityData {
    private float currentBlood = 0;
    public static final String BLOOD_KEY = "blood";

    public float getBlood() {
        return this.currentBlood;
    }

    public void setBlood(float amount) {
        this.currentBlood = Math.max(0, amount);
    }

    public void modifyBloodValue(float amount) {
        setBlood(getBlood() + amount);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat(BLOOD_KEY, this.currentBlood);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.currentBlood = nbt.getFloat(BLOOD_KEY);
    }

    @Override
    public Predicate<Entity> predicate() {
        return e -> e instanceof Player;
    }
}