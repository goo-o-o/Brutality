package net.goo.brutality.common.entity.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public class EntityStickyBombCap implements IBrutalityData {
    // Map: Player UUID -> Number of bombs they have on THIS entity
    private final Map<UUID, Integer> counts = new HashMap<>();

    @Override
    public Predicate<Entity> predicate() {
        // Any living entity can be "stuck" with a bomb
        return entity -> entity instanceof LivingEntity;
    }

    /**
     * @return The total number of bombs on this entity from all players.
     */
    public int getTotalCount() {
        return counts.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * @return The number of bombs a specific player has on this entity.
     */
    public int getCountForPlayer(UUID playerUuid) {
        return counts.getOrDefault(playerUuid, 0);
    }

    /**
     * Adds a bomb for a player, respecting a maximum limit.
     * @return true if the bomb was successfully added.
     */
    public boolean addBomb(UUID playerUuid) {
        int current = getCountForPlayer(playerUuid);
        counts.put(playerUuid, current + 1);
        return true;
    }

    public void removeAllBombs() {
        counts.clear();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        counts.forEach((uuid, count) -> {
            if (count > 0) tag.putInt(uuid.toString(), count);
        });
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        counts.clear();
        for (String key : nbt.getAllKeys()) {
            counts.put(UUID.fromString(key), nbt.getInt(key));
        }
    }
}