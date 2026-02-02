package net.goo.brutality.common.item.curios.charm;

import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;

public class RespawnCard extends BrutalityCurioItem {
    public RespawnCard(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    // Buys your way out of death.
    public static void tryCheatDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
                ItemCooldowns cooldowns = player.getCooldowns();
                if (handler.isEquipped(BrutalityItems.EVIL_KING_RESPAWN_CARD.get()) && !cooldowns.isOnCooldown(BrutalityItems.EVIL_KING_RESPAWN_CARD.get())) {
                    cooldowns.addCooldown(BrutalityItems.EVIL_KING_RESPAWN_CARD.get(), 15 * 60 * 20);
                    event.setCanceled(true);
                    player.setHealth(6F);

                } else if (handler.isEquipped(BrutalityItems.DIAMOND_RESPAWN_CARD.get()) && !cooldowns.isOnCooldown(BrutalityItems.DIAMOND_RESPAWN_CARD.get())) {
                    cooldowns.addCooldown(BrutalityItems.DIAMOND_RESPAWN_CARD.get(), 30 * 60 * 20);
                    event.setCanceled(true);
                    player.setHealth(4F);

                } else {
                    handler.findFirstCurio(BrutalityItems.SILVER_RESPAWN_CARD.get()).ifPresent(slot -> {
                        slot.stack().hurtAndBreak(slot.stack().getMaxDamage(), player, (p) -> {
                        });
                        event.setCanceled(true);
                        player.setHealth(2F);

                    });
                }

            });
        }
    }
}
