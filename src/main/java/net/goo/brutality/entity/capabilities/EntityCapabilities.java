package net.goo.brutality.entity.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityCapabilities {
    private static final String MIRACLE_BLIGHTED = "isMiracleBlighted", SHOULD_ROTATE = "shouldRotate", RAGE_VALUE = "rageValue", MANA_VALUE = "manaValue";

    @AutoRegisterCapability
    public static class EntityEffectCap implements INBTSerializable<CompoundTag> {
        private boolean miracleBlighted = false;

        public boolean isMiracleBlighted() {
            return this.miracleBlighted;
        }

        public void setMiracleBlighted(boolean active) {
            this.miracleBlighted = active;
        }

        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putBoolean(MIRACLE_BLIGHTED, isMiracleBlighted());
            return tag;
        }


        public void deserializeNBT(CompoundTag nbt) {
            setMiracleBlighted(nbt.getBoolean(MIRACLE_BLIGHTED));
        }
    }

    @AutoRegisterCapability
    public static class EntityShouldRotateCap implements INBTSerializable<CompoundTag> {
        private boolean shouldRotate = false;

        public boolean isShouldRotate() {
            return this.shouldRotate;
        }

        public void setShouldRotate(boolean active) {
            this.shouldRotate = active;
        }

        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putBoolean(SHOULD_ROTATE, isShouldRotate());
            return tag;
        }


        public void deserializeNBT(CompoundTag nbt) {
            setShouldRotate(nbt.getBoolean(SHOULD_ROTATE));
        }
    }


    @AutoRegisterCapability
    public static class EntityStarCountCap implements INBTSerializable<CompoundTag> {
        private final Map<UUID, Integer> playerStarCounts = new HashMap<>();

        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            playerStarCounts.forEach((uuid, count) -> tag.putInt(uuid.toString(), count));
            return tag;
        }

        public void deserializeNBT(CompoundTag nbt) {
            playerStarCounts.clear();
            for (String key : nbt.getAllKeys()) {
                try {
                    UUID uuid = UUID.fromString(key);
                    playerStarCounts.put(uuid, nbt.getInt(key));
                } catch (IllegalArgumentException ignored) {

                }
            }

        }

        public Map<UUID, Integer> getAllStarCounts() {
            return playerStarCounts;
        }

        public int getStarCount(UUID playerId) {
            return this.playerStarCounts.getOrDefault(playerId, 0);
        }

        public void incrementStarCount(UUID playerId) {
            setStarCount(playerId, getStarCount(playerId) + 1);
        }


        public void setStarCount(UUID playerId, int count) {
            playerStarCounts.put(playerId, count);
        }

        public void clearStarCount(UUID playerId) {
            playerStarCounts.remove(playerId);
        }
    }

    @AutoRegisterCapability
    public static class PlayerRageCap implements INBTSerializable<CompoundTag> {
        private float rageValue = 0;

        public float rageValue() {
            return this.rageValue;
        }

        public void setRageValue(float rageValue) {
            this.rageValue = rageValue;
        }

        public void incrementRage(float amount) {
            this.rageValue = rageValue() + amount;
        }

        public void decrementRage(float amount) {
            this.rageValue = rageValue() - amount;
        }

        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putFloat(RAGE_VALUE, rageValue());
            return tag;
        }


        public void deserializeNBT(CompoundTag nbt) {
            setRageValue(nbt.getInt(RAGE_VALUE));
        }
    }

    @AutoRegisterCapability
    public static class PlayerManaCap implements INBTSerializable<CompoundTag> {
        private float manaValue = 0;

        public float manaValue() {
            return this.manaValue;
        }

        public void setManaValue(float manaValue) {
            this.manaValue = manaValue;
        }

        public void incrementMana(float amount) {
            this.manaValue = manaValue() + amount;
        }

        public void decrementMana(float amount) {
            this.manaValue = manaValue() - amount;
        }

        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putFloat(MANA_VALUE, manaValue());
            return tag;
        }


        public void deserializeNBT(CompoundTag nbt) {
            setManaValue(nbt.getInt(MANA_VALUE));
        }
    }


}
