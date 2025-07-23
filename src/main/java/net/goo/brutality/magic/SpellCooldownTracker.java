package net.goo.brutality.magic;

import net.goo.brutality.Brutality;
import net.goo.brutality.registry.ModAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;

import java.util.*;

public class SpellCooldownTracker {
    // Stores cooldowns as player UUID -> (spell ID -> remaining ticks)
    private static final Map<UUID, Map<ResourceLocation, Integer>> playerCooldowns = new HashMap<>();

    // Called every tick for each player (register to PlayerTickEvent)
    public static void tick(Player player) {
        UUID playerId = player.getUUID();
        if (!playerCooldowns.containsKey(playerId)) return;

        Map<ResourceLocation, Integer> cooldowns = playerCooldowns.get(playerId);
        if (cooldowns == null) return;

        // Create iterator to safely remove expired cooldowns
        Iterator<Map.Entry<ResourceLocation, Integer>> iterator = cooldowns.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<ResourceLocation, Integer> entry = iterator.next();
            int remaining = entry.getValue() - 1;

            if (remaining <= 0) {
                iterator.remove();
            } else {
                entry.setValue(remaining);
            }
        }

        // Remove empty maps to clean up
        if (cooldowns.isEmpty()) {
            playerCooldowns.remove(playerId);
        }
    }

    public static void setCooldown(Player player, IBrutalitySpell spell) {
        AttributeInstance cdrInstance = player.getAttribute(ModAttributes.SPELL_COOLDOWN_REDUCTION.get());
        int cooldownTicks = cdrInstance != null ? (int) (spell.getBaseCooldown() * (1 - cdrInstance.getValue())) : spell.getBaseCooldown();

        if (cooldownTicks <= 0) return;

        playerCooldowns.computeIfAbsent(player.getUUID(), k -> new HashMap<>())
                .put(getSpellId(spell), cooldownTicks);
    }

    public static boolean isOnCooldown(Player player, IBrutalitySpell spell) {
        Map<ResourceLocation, Integer> cooldowns = playerCooldowns.get(player.getUUID());
        if (cooldowns == null) return false;
        return cooldowns.containsKey(getSpellId(spell));
    }

    public static int getRemainingTicks(Player player, IBrutalitySpell spell) {
        Map<ResourceLocation, Integer> cooldowns = playerCooldowns.get(player.getUUID());
        if (cooldowns == null) return 0;
        return cooldowns.getOrDefault(getSpellId(spell), 0);
    }

    private static ResourceLocation getSpellId(IBrutalitySpell spell) {
        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, spell.getSpellName().toLowerCase(Locale.ROOT));
    }
}