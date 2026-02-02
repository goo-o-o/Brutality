package net.goo.brutality.common.entity.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.function.Predicate;

@AutoRegisterCapability
public class PlayerManaCap implements IBrutalityData {
    private float currentMana = 0;
    public static final String MANA_KEY = "mana";

    public float getMana() {
        return this.currentMana;
    }

    public void setMana(float amount) {
        this.currentMana = Math.max(0, amount);
    }

    public void modifyManaValue(float amount) {
        setMana(getMana() + amount);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat(MANA_KEY, this.currentMana);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.currentMana = nbt.getFloat(MANA_KEY);
    }

    @Override
    public Predicate<Entity> predicate() {
        return e -> e instanceof Player;
    }
}