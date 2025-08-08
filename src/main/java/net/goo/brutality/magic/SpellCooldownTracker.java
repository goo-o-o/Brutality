package net.goo.brutality.magic;

import net.goo.brutality.Brutality;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.network.s2cSyncCooldownPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;
@Mod.EventBusSubscriber (bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpellCooldownTracker {
    private static final Map<UUID, Map<ResourceLocation, Integer>> serverCooldowns = new HashMap<>();
    private static final Map<UUID, Map<ResourceLocation, Integer>> serverOriginalCooldowns = new HashMap<>();
    private static final Map<UUID, Map<ResourceLocation, Integer>> clientCooldowns = new HashMap<>();
    private static final Map<UUID, Map<ResourceLocation, Integer>> clientOriginalCooldowns = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        UUID playerId = player.getUUID();

        if (!player.level().isClientSide()) {
            Map<ResourceLocation, Integer> cooldowns = serverCooldowns.get(playerId);
            if (cooldowns == null) return;

            Iterator<Map.Entry<ResourceLocation, Integer>> iterator = cooldowns.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<ResourceLocation, Integer> entry = iterator.next();
                int remaining = entry.getValue() - 1;
                if (remaining <= 0) {
                    iterator.remove();
                    serverOriginalCooldowns.getOrDefault(playerId, Collections.emptyMap()).remove(entry.getKey());
                } else {
                    entry.setValue(remaining);
                }
            }

            if (cooldowns.isEmpty()) {
                serverCooldowns.remove(playerId);
                serverOriginalCooldowns.remove(playerId);
            }
        } else {
            Map<ResourceLocation, Integer> cooldowns = clientCooldowns.get(playerId);
            if (cooldowns == null) return;

            Iterator<Map.Entry<ResourceLocation, Integer>> iterator = cooldowns.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<ResourceLocation, Integer> entry = iterator.next();
                int remaining = entry.getValue() - 1;
                if (remaining <= 0) {
                    iterator.remove();
                    clientOriginalCooldowns.getOrDefault(playerId, Collections.emptyMap()).remove(entry.getKey());
                } else {
                    entry.setValue(remaining);
                }
            }

            if (cooldowns.isEmpty()) {
                clientCooldowns.remove(playerId);
                clientOriginalCooldowns.remove(playerId);
            }
        }
    }

    public static void setCooldown(Player player, IBrutalitySpell spell, int spellLevel) {
        if (!player.level().isClientSide()) {
            int cooldownTicks = SpellCastingHandler.getActualCooldown(player, spell, spellLevel);
            if (cooldownTicks <= 0) return;

            ResourceLocation spellId = getSpellId(spell);
            serverCooldowns.computeIfAbsent(player.getUUID(), k -> new HashMap<>()).put(spellId, cooldownTicks);
            serverOriginalCooldowns.computeIfAbsent(player.getUUID(), k -> new HashMap<>()).put(spellId, cooldownTicks);
            PacketHandler.sendToPlayer(new s2cSyncCooldownPacket(spellId.toString(), cooldownTicks, cooldownTicks), (ServerPlayer) player);
        }
    }

    public static void resetCooldowns(Player player) {
        UUID playerId = player.getUUID();
        Map<ResourceLocation, Integer> cooldowns = serverCooldowns.get(playerId);

        if (cooldowns != null) {
            cooldowns.replaceAll((spellId, value) -> 0);
            cooldowns.forEach((spellId, value) -> PacketHandler.sendToPlayer(
                    new s2cSyncCooldownPacket(spellId.toString(), 0 ,0), (ServerPlayer) player));
            if (cooldowns.isEmpty()) {
                serverCooldowns.remove(playerId);
                serverOriginalCooldowns.remove(playerId);
            }
        }
    }

    public static boolean isOnCooldown(Player player, IBrutalitySpell spell) {
        if (!player.level().isClientSide()) {
            return serverCooldowns.getOrDefault(player.getUUID(), Collections.emptyMap())
                    .containsKey(getSpellId(spell));
        } else {
            return clientCooldowns.getOrDefault(player.getUUID(), Collections.emptyMap())
                    .containsKey(getSpellId(spell));
        }
    }

    public static int getRemainingTicks(Player player, IBrutalitySpell spell) {
        if (!player.level().isClientSide()) {
            return serverCooldowns.getOrDefault(player.getUUID(), Collections.emptyMap())
                    .getOrDefault(getSpellId(spell), 0);
        } else {
            return clientCooldowns.getOrDefault(player.getUUID(), Collections.emptyMap())
                    .getOrDefault(getSpellId(spell), 0);
        }
    }

    public static float getCooldownProgress(Player player, IBrutalitySpell spell) {
        Map<ResourceLocation, Integer> cooldowns = !player.level().isClientSide() ?
                serverCooldowns.get(player.getUUID()) : clientCooldowns.get(player.getUUID());
        Map<ResourceLocation, Integer> originals = !player.level().isClientSide() ?
                serverOriginalCooldowns.get(player.getUUID()) : clientOriginalCooldowns.get(player.getUUID());

        if (cooldowns == null || originals == null) return 0f;

        Integer remaining = cooldowns.get(getSpellId(spell));
        Integer original = originals.get(getSpellId(spell));

        if (remaining == null || original == null || original == 0) return 0f;

        return (float) remaining / original;
    }

    public static void updateCooldownClient(String spellId, int remaining, int original) {
        if (Minecraft.getInstance().player != null) {
            ResourceLocation id = ResourceLocation.parse(spellId);
            UUID playerId = Minecraft.getInstance().player.getUUID();
            if (remaining > 0) {
                clientCooldowns.computeIfAbsent(playerId, k -> new HashMap<>()).put(id, remaining);
                clientOriginalCooldowns.computeIfAbsent(playerId, k -> new HashMap<>()).put(id, original);
            } else {
                clientCooldowns.getOrDefault(playerId, Collections.emptyMap()).remove(id);
                clientOriginalCooldowns.getOrDefault(playerId, Collections.emptyMap()).remove(id);
            }
        }
    }

    private static ResourceLocation getSpellId(IBrutalitySpell spell) {
        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, spell.getSpellName().toLowerCase(Locale.ROOT));
    }
}