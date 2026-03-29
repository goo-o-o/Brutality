package net.goo.brutality.common.entity.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.function.Predicate;

@AutoRegisterCapability
public class PlayerBloodCap implements IBrutalityData {
    private float currentBlood = 0;
    public static float maxBlood = 100;
    public static final String BLOOD_KEY = "blood";

    public float getBlood() {
        return this.currentBlood;
    }

    public void setBlood(Player player, float amount) {
        this.currentBlood = Mth.clamp(0, amount, maxBlood);
        BrutalityCapabilities.sync(player, BrutalityCapabilities.BLOOD);
    }

    public void modifyBloodValue(Player player, float amount) {
        setBlood(player, getBlood() + amount);
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

    public float getCurrentBloodPercentage() {
        return getBlood() / maxBlood;
    }
}