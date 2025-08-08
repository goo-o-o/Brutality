package net.goo.brutality.entity.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityCapabilities {
    private static final String MIRACLE_BLIGHTED = "isMiracleBlighted", SHOULD_ROTATE = "shouldRotate", RAGE_VALUE = "rageValue", MANA_VALUE = "manaValue";
    private static final String IS_RAGE = "isRage", IS_THE_VOID = "isTheVoid", IS_LIGHT_BOUND = "isBound";
    private static final String HIT_COUNT = "hitCount", LAST_HIT_TIME = "lastHitTime", LAST_VICTIM_ID = "lastVictimId";

    @AutoRegisterCapability
    public static class EntityEffectCap implements INBTSerializable<CompoundTag> {
        private boolean miracleBlighted = false;
        private boolean isRage = false;
        private boolean isTheVoid = false;
        private boolean isLightBound = false;

        public boolean isMiracleBlighted() {
            return this.miracleBlighted;
        }

        public boolean isRage() {
            return this.isRage;
        }

        public boolean isTheVoid() {
            return this.isTheVoid;
        }

        public boolean isLightBound() {
            return this.isLightBound;
        }

        public void setLightBound(boolean lightBound) {
            this.isLightBound = lightBound;
        }

        public void setMiracleBlighted(boolean active) {
            this.miracleBlighted = active;
        }

        public void setRage(boolean active) {
            this.isRage = active;
        }

        public void setTheVoid(boolean active) {
            this.isTheVoid = active;
        }


        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putBoolean(MIRACLE_BLIGHTED, isMiracleBlighted());
            tag.putBoolean(IS_RAGE, isRage());
            tag.putBoolean(IS_THE_VOID, isTheVoid());
            tag.putBoolean(IS_LIGHT_BOUND, isLightBound());
            return tag;
        }


        public void deserializeNBT(CompoundTag nbt) {
            setMiracleBlighted(nbt.getBoolean(MIRACLE_BLIGHTED));
            setRage(nbt.getBoolean(IS_RAGE));
            setTheVoid(nbt.getBoolean(IS_THE_VOID));
            setLightBound(nbt.getBoolean(IS_LIGHT_BOUND));
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


    @AutoRegisterCapability
    public static class PlayerComboCap implements INBTSerializable<CompoundTag> {
        private int lastVictimId;
        private int hitCount;
        private long lastHitTime;

        public void resetAll() {
            lastVictimId = -10;
            hitCount = 0;
            lastHitTime = 0;
        }

        public int lastVictimId() {
            return lastVictimId;
        }

        public void setLastVictimId(int lastVictimId) {
            this.lastVictimId = lastVictimId;
        }

        public int hitCount() {
            return hitCount;
        }

        public void setHitCount(int hitCount) {
            this.hitCount = hitCount;
        }

        public long lastHitTime() {
            return lastHitTime;
        }

        public void setLastHitTime(long lastHitTime) {
            this.lastHitTime = lastHitTime;
        }

        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putInt("LastVictim", lastVictimId);
            tag.putInt("HitCount", hitCount);
            tag.putLong("LastHitTime", lastHitTime);
            return tag;
        }

        public void deserializeNBT(CompoundTag nbt) {
            setHitCount(nbt.getInt(HIT_COUNT));
            setLastHitTime(nbt.getLong(LAST_HIT_TIME));
            setLastVictimId(nbt.getInt(LAST_VICTIM_ID));
        }
    }
}
