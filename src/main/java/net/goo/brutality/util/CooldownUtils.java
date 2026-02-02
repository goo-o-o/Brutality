package net.goo.brutality.util;

import com.google.common.base.Suppliers;
import net.goo.brutality.common.network.PacketHandler;
import net.goo.brutality.common.network.clientbound.ClientboundSyncItemCooldownPacket;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.event.forge.DelayedTaskScheduler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraftforge.event.entity.player.PlayerEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class CooldownUtils {
    /**
     * Transfers item cooldowns from the original player to the new player upon death,
     * ensuring specific cooldowns persist post-respawn, and synchronizes the data
     * with the client if necessary.
     *
     * @param event The PlayerEvent.Clone event that provides access to the old and new player entities,
     *              as well as information about whether the cloning was a result of death.
     */
    public static void persistCooldowns(PlayerEvent.Clone event) {
        Player newPlayer = event.getEntity();
        Player oldPlayer = event.getOriginal();
        if (event.isWasDeath()) {
            ItemCooldowns newCooldowns = newPlayer.getCooldowns();
            ItemCooldowns oldCooldowns = oldPlayer.getCooldowns();

            boolean shouldSync = false;
            for (Map.Entry<Item, ItemCooldowns.CooldownInstance> entry : oldCooldowns.cooldowns.entrySet()) {
                Item item = entry.getKey();

                if (CooldownUtils.CD_PERSIST_ITEMS.get().contains(item)) {
                    ItemCooldowns.CooldownInstance cooldown = entry.getValue();
                    newCooldowns.cooldowns.put(item, new ItemCooldowns.CooldownInstance(cooldown.startTime, cooldown.endTime));
                    newCooldowns.tickCount = oldCooldowns.tickCount;
                    shouldSync = true;
                }
            }

            if (shouldSync)
                DelayedTaskScheduler.queueServerWork(newPlayer.level(), 1, () ->
                        PacketHandler.sendToPlayerClient(new ClientboundSyncItemCooldownPacket(newCooldowns.cooldowns, newCooldowns.tickCount), ((ServerPlayer) newPlayer)));
        }
    }

    public static final Supplier<List<Item>> CD_PERSIST_ITEMS = Suppliers.memoize(() -> List.of(
            BrutalityItems.EVIL_KING_BOOSTER_PACK.get(), BrutalityItems.EVIL_KING_RESPAWN_CARD.get(),
            BrutalityItems.DIAMOND_BOOSTER_PACK.get(), BrutalityItems.DIAMOND_RESPAWN_CARD.get(),
            BrutalityItems.SILVER_BOOSTER_PACK.get(), BrutalityItems.SILVER_RESPAWN_CARD.get()
    ));

    public static void validateCurioCooldown(Player player, Item item, int cooldownTicks, Runnable runnable) {
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            if (handler.isEquipped(item)) {
                validateCooldown(player, item, cooldownTicks, runnable);
            }
        });
    }

    public static void validateCooldown(Player player, Item item, int cooldownTicks, Runnable runnable) {
        if (!player.getCooldowns().isOnCooldown(item)) {
            runnable.run();
            player.getCooldowns().addCooldown(item, cooldownTicks);
        }
    }
}
