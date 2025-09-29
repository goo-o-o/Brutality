package net.goo.brutality.magic;

import net.goo.brutality.Brutality;
import net.goo.brutality.network.ClientboundSyncSpellCooldownPacket;
import net.goo.brutality.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpellCooldownTracker {
    private static final Map<UUID, Map<ResourceLocation, CooldownData>> serverCooldowns = new HashMap<>();
    private static final Map<UUID, Map<ResourceLocation, CooldownData>> clientCooldowns = new HashMap<>();
    private static final Map<UUID, Integer> syncTickCounters = new HashMap<>();


    public record CooldownData(int remainingTicks, int originalTicks, float progress) {
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.side.isClient()) return;
        Player player = event.player;
        UUID playerId = player.getUUID();

        Map<ResourceLocation, CooldownData> cooldowns = serverCooldowns.get(playerId);
        if (cooldowns == null) return;

        int tickCounter = syncTickCounters.getOrDefault(playerId, 0) + 1;
        boolean shouldSync = tickCounter >= 10;


        Iterator<Map.Entry<ResourceLocation, CooldownData>> iterator = cooldowns.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<ResourceLocation, CooldownData> entry = iterator.next();
            CooldownData data = entry.getValue();
            int remaining = data.remainingTicks - 1;
            int originalTicks = data.originalTicks;

            if (remaining <= 0) {
                iterator.remove();
                if (shouldSync) {
                    PacketHandler.sendToPlayer(new ClientboundSyncSpellCooldownPacket(entry.getKey().toString(), 0, 0, 0), (ServerPlayer) player);
                }
            } else {
                float newProgress = (float) (originalTicks - remaining) / originalTicks;
                entry.setValue(new CooldownData(remaining, data.originalTicks, newProgress));
                if (shouldSync) {
                    PacketHandler.sendToPlayer(new ClientboundSyncSpellCooldownPacket(entry.getKey().toString(), remaining, data.originalTicks, newProgress), (ServerPlayer) player);
                }
            }
        }

        if (cooldowns.isEmpty()) {
            serverCooldowns.remove(playerId);
            syncTickCounters.remove(playerId);
        } else if (shouldSync) {
            syncTickCounters.put(playerId, 0);
        } else {
            syncTickCounters.put(playerId, tickCounter);
        }
    }


    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onClientPlayerTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || Minecraft.getInstance().player == null) return;
        Player player = Minecraft.getInstance().player;
        UUID playerId = player.getUUID();

        Map<ResourceLocation, CooldownData> cooldowns = clientCooldowns.get(playerId);
        if (cooldowns == null) return;

        Iterator<Map.Entry<ResourceLocation, CooldownData>> iterator = cooldowns.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<ResourceLocation, CooldownData> entry = iterator.next();
            CooldownData data = entry.getValue();
            int remaining = data.remainingTicks - 1;
            int originalTicks = data.originalTicks;

            if (remaining <= 0) {
                iterator.remove();
            } else {
                float newProgress = (float) (originalTicks - remaining) / originalTicks;
                entry.setValue(new CooldownData(remaining, data.originalTicks, newProgress));
            }
        }

        if (cooldowns.isEmpty()) {
            clientCooldowns.remove(playerId);
        }


    }


    public static void setCooldown(Player player, IBrutalitySpell spell, int spellLevel) {
        int cooldownTicks = IBrutalitySpell.getActualCooldown(player, spell, spellLevel);
        if (cooldownTicks <= 0) {
            return;
        }

        ResourceLocation spellId = getSpellId(spell);
        CooldownData data = new CooldownData(cooldownTicks, cooldownTicks, 0f);

        if (!player.level().isClientSide()) {
            Map<ResourceLocation, CooldownData> cooldowns = serverCooldowns.computeIfAbsent(player.getUUID(), k -> new HashMap<>());
            cooldowns.put(spellId, data);
            PacketHandler.sendToPlayer(new ClientboundSyncSpellCooldownPacket(spellId.toString(), cooldownTicks, cooldownTicks, 0f), (ServerPlayer) player);
        } else {
            clientCooldowns.computeIfAbsent(player.getUUID(), k -> new HashMap<>()).put(spellId, data);
        }
    }

    public static void resetCooldowns(Player player) {
        UUID playerId = player.getUUID();
        if (!player.level().isClientSide()) {
            Map<ResourceLocation, CooldownData> cooldowns = serverCooldowns.get(playerId);
            if (cooldowns != null) {
                cooldowns.keySet().forEach(spellId ->
                        PacketHandler.sendToPlayer(new ClientboundSyncSpellCooldownPacket(spellId.toString(), 0, 0, 0), (ServerPlayer) player));
                serverCooldowns.remove(playerId);
            }
        } else {
            clientCooldowns.remove(playerId);
        }
    }

    public static boolean isOnCooldown(Player player, IBrutalitySpell spell) {
        ResourceLocation spellId = getSpellId(spell);
        Map<UUID, Map<ResourceLocation, CooldownData>> cooldownMap = player.level().isClientSide() ? clientCooldowns : serverCooldowns;
        return cooldownMap.getOrDefault(player.getUUID(), Collections.emptyMap()).containsKey(spellId);
    }

    public static int getRemainingTicks(Player player, IBrutalitySpell spell) {
        ResourceLocation spellId = getSpellId(spell);
        Map<UUID, Map<ResourceLocation, CooldownData>> cooldownMap = player.level().isClientSide() ? clientCooldowns : serverCooldowns;
        CooldownData data = cooldownMap.getOrDefault(player.getUUID(), Collections.emptyMap()).get(spellId);
        return data != null ? data.remainingTicks : 0;
    }

    public static float getCooldownProgress(Player player, IBrutalitySpell spell) {
        ResourceLocation spellId = getSpellId(spell);
        CooldownData data = clientCooldowns.getOrDefault(player.getUUID(), Collections.emptyMap()).get(spellId);
        return data != null ? Math.max(0f, Math.min(1f, data.progress)) : 0f;
    }

    @OnlyIn(Dist.CLIENT)
    public static void updateCooldownClient(String spellId, int remaining, int original, float progress) {
        if (Minecraft.getInstance().player != null) {
            ResourceLocation id = ResourceLocation.parse(spellId);
            UUID playerId = Minecraft.getInstance().player.getUUID();
            if (remaining > 0) {
                float clampedProgress = Math.max(0f, Math.min(1f, progress));
                clientCooldowns.computeIfAbsent(playerId, k -> new HashMap<>()).put(id, new CooldownData(remaining, original, clampedProgress));
            } else {
                clientCooldowns.getOrDefault(playerId, Collections.emptyMap()).remove(id);
            }
        }
    }

    private static ResourceLocation getSpellId(IBrutalitySpell spell) {
        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, spell.getSpellName().toLowerCase(Locale.ROOT));
    }
}