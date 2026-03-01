package net.goo.brutality.util.magic;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.entity.capabilities.BrutalityCapabilities;
import net.goo.brutality.common.entity.capabilities.PlayerManaCap;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ManaHelper {
    @SubscribeEvent
    public static void manaTicker(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (event.phase == TickEvent.Phase.START) {
            float currentMana = ManaHelper.getMana(player);
            float maxMana = (float) player.getAttributeValue(BrutalityAttributes.MAX_MANA.get());
            if (currentMana < maxMana) {
                float regenPerTick = (float) player.getAttributeValue(BrutalityAttributes.MANA_REGEN.get()) / 20f;
                float newMana = Math.min(currentMana + regenPerTick, maxMana);
                float actualChange = newMana - currentMana;

                if (actualChange > 0) {
                    if (player.tickCount % 20 == 0) {
                        ManaHelper.modifyManaValue(player, actualChange);
                    } else {
                        ManaHelper.modifyManaValueNoSync(player, actualChange);
                    }
                }
            }
        }
    }

    /**
     * Retrieves the current mana of the specified player. If the player is a server-side player,
     * the mana capability will be synchronized to the client before determining the mana value.
     *
     * @param player The player whose mana value is to be retrieved. The player must have the
     *               `BrutalityCapabilities.MANA` capability attached to access mana data.
     * @return The current mana value of the player as a float. If the mana capability is not
     * present or accessible, it returns 0.
     * Always use this instead of the capability itself
     */
    public static float getMana(Player player) {
        return player.getCapability(BrutalityCapabilities.MANA).map(PlayerManaCap::getMana).orElse(0F);
    }

    /**
     * Modifies the mana of a specified player by a given amount. This method accesses the player's
     * Mana capability to apply the modification. If the player is a server-side player, the updated
     * mana value is synchronized with the client to ensure consistency.
     *
     * @param player The player whose mana will be modified. The player must have the
     *               `BrutalityCapabilities.MANA` capability attached to access mana data.
     * @param amount The amount by which to modify the player's mana. This can be a positive value
     *               to increase mana or a negative value to decrease mana.
     */
    public static void modifyManaValue(Player player, float amount) {
        modifyManaValueNoSync(player, amount);
        BrutalityCapabilities.sync(player, BrutalityCapabilities.MANA);
    }

    public static void modifyManaValueNoSync(Player player, float amount) {
        player.getCapability(BrutalityCapabilities.MANA).ifPresent(cap -> {
            cap.modifyManaValue(amount);
        });
    }

    /**
     * Calculates the current mana percentage for a given player.
     * If the player is a server-side player, the mana capability is synchronized to the client
     * before retrieving the mana value.
     *
     * @param player The player whose current mana percentage is being calculated.
     *               The player must have the `BrutalityCapabilities.MANA` capability attached to
     *               access mana data.
     * @return The current mana of the player as a percentage of their maximum mana, represented as a float.
     * If the mana capability is not present or accessible, it returns 0.
     */
    public static float getCurrentManaPercentage(Player player) {
        return (float) (getMana(player) / player.getAttributeValue(BrutalityAttributes.MAX_MANA.get()));
    }

    public static boolean isMaxMana(Player player) {
        return getCurrentManaPercentage(player) >= 1;
    }

}
