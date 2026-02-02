package net.goo.brutality.common.entity.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

@AutoRegisterCapability
public class PlayerSpellCooldownsCap implements IBrutalityData {
    // Now correctly mapped to the record
    private final Map<ResourceLocation, CooldownEntry> cooldowns = new HashMap<>();

    public record CooldownEntry(int remaining, int total) {}

    public void setCooldown(ResourceLocation spellId, int ticks) {
        if (ticks <= 0) {
            cooldowns.remove(spellId);
        } else {
            // When setting a cooldown, we assume the 'ticks' provided is the 'total'
            cooldowns.put(spellId, new CooldownEntry(ticks, ticks));
        }
    }

    public int getRemaining(ResourceLocation spellId) {
        CooldownEntry entry = cooldowns.get(spellId);
        return entry != null ? entry.remaining() : 0;
    }

    public CooldownEntry getEntry(ResourceLocation spellId) {
        return cooldowns.get(spellId);
    }

    // Expose the internal map for the Iterator in SpellCooldownTracker
    public Map<ResourceLocation, CooldownEntry> getAll() {
        return cooldowns;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag root = new CompoundTag();
        cooldowns.forEach((id, entry) -> {
            if (entry.remaining() > 0) {
                CompoundTag spellTag = new CompoundTag();
                spellTag.putInt("remaining", entry.remaining());
                spellTag.putInt("total", entry.total());
                root.put(id.toString(), spellTag);
            }
        });
        return root;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        cooldowns.clear();
        for (String key : nbt.getAllKeys()) {
            CompoundTag spellTag = nbt.getCompound(key);
            cooldowns.put(ResourceLocation.parse(key), new CooldownEntry(
                    spellTag.getInt("remaining"),
                    spellTag.getInt("total")
            ));
        }
    }

    @Override
    public Predicate<Entity> predicate() {
        return e -> e instanceof Player;
    }
}