package net.goo.brutality.util.magic;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.entity.capabilities.BrutalityCapabilities;
import net.goo.brutality.common.entity.capabilities.PlayerSpellCooldownsCap;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.registry.BrutalitySpells;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Iterator;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class SpellCooldownTracker {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        // Run on both sides so the client can predict the countdown
        if (event.phase != TickEvent.Phase.END) return;

        Player player = event.player;

        player.getCapability(BrutalityCapabilities.SPELL_COOLDOWNS).ifPresent(cap -> {
            var cooldowns = cap.getAll();
            if (cooldowns.isEmpty()) return;

            boolean anyFinished = false;
            Iterator<Map.Entry<ResourceLocation, PlayerSpellCooldownsCap.CooldownEntry>> it = cooldowns.entrySet().iterator();

            while (it.hasNext()) {
                var entry = it.next();
                int nextRemaining = entry.getValue().remaining() - 1;

                if (nextRemaining <= 0) {
                    it.remove();
                    anyFinished = true;
                } else {
                    entry.setValue(new PlayerSpellCooldownsCap.CooldownEntry(nextRemaining, entry.getValue().total()));
                }
            }

            // --- SYNCHRONIZATION LOGIC (Server Side Only) ---
            if (!player.level().isClientSide) {
                // 1. Immediate sync if a spell finished (to ensure the HUD clears)
                // 2. Periodic sync every 20 ticks to correct client drift
                if (anyFinished || player.tickCount % 20 == 0) {
                    BrutalityCapabilities.sync(player, BrutalityCapabilities.SPELL_COOLDOWNS);
                }
            }
        });
    }

    public static void setCooldown(Player player, BrutalitySpell spell, int ticks) {
        ResourceLocation id = BrutalitySpells.getIdFromSpell(spell);
        player.getCapability(BrutalityCapabilities.SPELL_COOLDOWNS).ifPresent(cap -> {
            cap.setCooldown(id, ticks);
            BrutalityCapabilities.sync(player, BrutalityCapabilities.SPELL_COOLDOWNS);
        });
    }

    public static void resetAllCooldowns(Player player) {
        player.getCapability(BrutalityCapabilities.SPELL_COOLDOWNS).ifPresent(cap -> {
            cap.getAll().clear();
            BrutalityCapabilities.sync(player, BrutalityCapabilities.SPELL_COOLDOWNS);
        });
    }

    public static float getRemainingCooldownPercentage(Player player, BrutalitySpell spell) {
        BrutalityCapabilities.sync(player, BrutalityCapabilities.SPELL_COOLDOWNS);
        return player.getCapability(BrutalityCapabilities.SPELL_COOLDOWNS).map(cap -> {
            var entry = cap.getEntry(BrutalitySpells.getIdFromSpell(spell));
            if (entry == null || entry.total() <= 0) return 0f;

            // This math is now level-independent because it uses the 'total' from the moment of casting
            return (float) entry.remaining() / (float) entry.total();
        }).orElse(0f);
    }

    public static int getRemainingTicks(Player player, BrutalitySpell spell) {
        BrutalityCapabilities.sync(player, BrutalityCapabilities.SPELL_COOLDOWNS);
        return player.getCapability(BrutalityCapabilities.SPELL_COOLDOWNS)
                .map(cap -> cap.getRemaining(BrutalitySpells.getIdFromSpell(spell))).orElse(0);
    }

    public static boolean isOnCooldown(Player player, BrutalitySpell spell) {
        return getRemainingTicks(player, spell) > 0;
    }
}