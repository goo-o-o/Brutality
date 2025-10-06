package net.goo.brutality.entity.capabilities;

import net.goo.brutality.event.ConsumeManaEvent;
import net.goo.brutality.magic.IBrutalitySpell;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.SealUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.*;

public class EntityCapabilities {
    private static final String MIRACLE_BLIGHTED = "isMiracleBlighted", SHOULD_ROTATE = "shouldRotate", RAGE_VALUE = "rageValue", MANA_VALUE = "manaValue";
    private static final String IS_RAGE = "isRage", IS_THE_VOID = "isTheVoid", IS_LIGHT_BOUND = "isBound";
    private static final String HIT_COUNT = "hitCount", LAST_HIT_TIME = "lastHitTime", LAST_VICTIM_ID = "lastVictimId";
    private static final String BOOSTER_TYPE = "boosterType", KIT_TYPE = "kitType";
    private static final String SEAL_TYPE = "seal_type";

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
    public static class RespawnCap implements INBTSerializable<CompoundTag> {
        public enum BOOSTER_TYPE {
            NONE,
            SILVER,
            DIAMOND,
            EVIL_KING
        }

        public enum KIT_TYPE {
            NONE,
            SILVER,
            DIAMOND,
            EVIL_KING
        }

        private BOOSTER_TYPE boosterType = RespawnCap.BOOSTER_TYPE.NONE;

        public BOOSTER_TYPE getBoosterType() {
            return this.boosterType;
        }

        public void setBoosterType(BOOSTER_TYPE boosterType) {
            this.boosterType = boosterType;
        }

        private KIT_TYPE kitType = RespawnCap.KIT_TYPE.NONE;

        public KIT_TYPE getKitType() {
            return this.kitType;
        }

        public void setKitType(KIT_TYPE kitType) {
            this.kitType = kitType;
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putString(BOOSTER_TYPE, boosterType.name()); // Serialize enum as string
            tag.putString(KIT_TYPE, kitType.name()); // Serialize enum as string
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            String name = nbt.getString(BOOSTER_TYPE);
            try {
                setBoosterType(RespawnCap.BOOSTER_TYPE.valueOf(name));
                setKitType(RespawnCap.KIT_TYPE.valueOf(name));
            } catch (IllegalArgumentException e) {
                setBoosterType(RespawnCap.BOOSTER_TYPE.NONE);
                setKitType(RespawnCap.KIT_TYPE.NONE);
            }
        }
    }


    @AutoRegisterCapability
    public static class EntitySealTypeCap implements INBTSerializable<CompoundTag> {


        private SealUtils.SEAL_TYPE sealType = SealUtils.SEAL_TYPE.NONE;

        public SealUtils.SEAL_TYPE getSealType() {
            return this.sealType;
        }

        public void setSealType(SealUtils.SEAL_TYPE sealType) {
            this.sealType = sealType;
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putString(BOOSTER_TYPE, sealType.name()); // Serialize enum as string
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            String name = nbt.getString(SEAL_TYPE);
            try {
                setSealType(SealUtils.SEAL_TYPE.valueOf(name));
            } catch (IllegalArgumentException e) {
                setSealType(SealUtils.SEAL_TYPE.NONE);
            }
        }
    }

    @AutoRegisterCapability
    public static class EntityStickyBombCap implements INBTSerializable<CompoundTag> {
        private final Map<UUID, Map<Integer, Integer>> playerStickyBombCounts = new HashMap<>();

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            playerStickyBombCounts.forEach((playerUuid, entityMap) -> {
                CompoundTag entityTag = new CompoundTag();
                entityMap.forEach((entityUuid, count) -> entityTag.putInt(entityUuid.toString(), count));
                tag.put(playerUuid.toString(), entityTag);
            });
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            playerStickyBombCounts.clear();
            for (String playerKey : nbt.getAllKeys()) {
                try {
                    UUID playerUuid = UUID.fromString(playerKey);
                    CompoundTag entityTag = nbt.getCompound(playerKey);
                    Map<Integer, Integer> entityMap = new HashMap<>();
                    for (String entityKey : entityTag.getAllKeys()) {
                        try {
                            Integer entityId = Integer.parseInt(entityKey);
                            entityMap.put(entityId, entityTag.getInt(entityKey));
                        } catch (NumberFormatException ignored) {}
                    }
                    playerStickyBombCounts.put(playerUuid, entityMap);
                } catch (IllegalArgumentException ignored) {}
            }
        }


        public Map<UUID, Map<Integer, Integer>> getAllStickyBombCounts() {
            return playerStickyBombCounts;
        }

        public int getStickyBombCount(UUID playerId, Integer entityId) {
            return playerStickyBombCounts.getOrDefault(playerId, new HashMap<>()).getOrDefault(entityId, 0);
        }

        public void incrementStickyBombCount(UUID playerId, Integer entityId) {
            setStickyBombCount(playerId, entityId, getStickyBombCount(playerId, entityId) + 1);
        }

        public void setStickyBombCount(UUID playerId, Integer entityId, int count) {
            playerStickyBombCounts.computeIfAbsent(playerId, k -> new HashMap<>()).put(entityId, count);
            if (count == 0) {
                playerStickyBombCounts.get(playerId).remove(entityId);
                if (playerStickyBombCounts.get(playerId).isEmpty()) {
                    playerStickyBombCounts.remove(playerId);
                }
            }
        }

        public void clearStickyBombCount(UUID playerId, Integer entityId) {
            setStickyBombCount(playerId, entityId, 0);
        }

        public Map<Integer, Integer> getEntitiesForPlayer(UUID playerId) {
            return playerStickyBombCounts.getOrDefault(playerId, new HashMap<>());
        }
    }
    @AutoRegisterCapability
    public static class EntityStarCountCap implements INBTSerializable<CompoundTag> {
        private final Map<UUID, Map<Integer, Integer>> playerStarCounts = new HashMap<>();

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            playerStarCounts.forEach((playerUuid, entityMap) -> {
                CompoundTag entityTag = new CompoundTag();
                entityMap.forEach((entityUuid, count) -> entityTag.putInt(entityUuid.toString(), count));
                tag.put(playerUuid.toString(), entityTag);
            });
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            playerStarCounts.clear();
            for (String playerKey : nbt.getAllKeys()) {
                try {
                    UUID playerUuid = UUID.fromString(playerKey);
                    CompoundTag entityTag = nbt.getCompound(playerKey);
                    Map<Integer, Integer> entityMap = new HashMap<>();
                    for (String entityKey : entityTag.getAllKeys()) {
                        try {
                            Integer entityId = Integer.parseInt(entityKey);
                            entityMap.put(entityId, entityTag.getInt(entityKey));
                        } catch (NumberFormatException ignored) {}
                    }
                    playerStarCounts.put(playerUuid, entityMap);
                } catch (IllegalArgumentException ignored) {}
            }
        }


        public Map<UUID, Map<Integer, Integer>> getAllStarCounts() {
            return playerStarCounts;
        }

        public int getStarCount(UUID playerId, Integer entityId) {
            return playerStarCounts.getOrDefault(playerId, new HashMap<>()).getOrDefault(entityId, 0);
        }

        public void incrementStarCount(UUID playerId, Integer entityId) {
            setStarCount(playerId, entityId, getStarCount(playerId, entityId) + 1);
        }

        public void setStarCount(UUID playerId, Integer entityId, int count) {
            playerStarCounts.computeIfAbsent(playerId, k -> new HashMap<>()).put(entityId, count);
            if (count == 0) {
                playerStarCounts.get(playerId).remove(entityId);
                if (playerStarCounts.get(playerId).isEmpty()) {
                    playerStarCounts.remove(playerId);
                }
            }
        }

        public void clearStarCount(UUID playerId, Integer entityId) {
            setStarCount(playerId, entityId, 0);
        }

        public Map<Integer, Integer> getEntitiesForPlayer(UUID playerId) {
            return playerStarCounts.getOrDefault(playerId, new HashMap<>());
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

        public boolean isMaxMana(Player player) {
            AttributeInstance maxMana = player.getAttribute(ModAttributes.MAX_MANA.get());
            if (maxMana != null) return this.manaValue == maxMana.getValue();
            return false;
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

        public float getCurrentManaRatio(Player player) {
            AttributeInstance maxMana = player.getAttribute(ModAttributes.MAX_MANA.get());
            if (maxMana != null) return (float) (this.manaValue / maxMana.getValue());
            return 0;
        }

        public void decrementMana(Player player, IBrutalitySpell spell, int spellLevel, float amount) {
            decrementMana(amount);
            MinecraftForge.EVENT_BUS.post(new ConsumeManaEvent(player, spell, spellLevel, amount));
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
