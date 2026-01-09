package net.goo.brutality.item.curios.charm;

import net.goo.brutality.item.curios.BrutalityCurioItem;
import net.goo.brutality.network.ClientboundSyncCapabilitiesPacket;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class SpiteShard extends BrutalityCurioItem {


    public SpiteShard(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player && player.level() instanceof ServerLevel) {
            if (player.tickCount % 20 == 0) {
                List<Mob> aggroEntities =
                        player.level().getNearbyEntities(Mob.class, TargetingConditions.forCombat().selector(e -> e instanceof Mob mob && mob.getTarget() == player),
                                player, player.getBoundingBox().inflate(5));

                if (!aggroEntities.isEmpty()) {
                    player.getCapability(BrutalityCapabilities.PLAYER_RAGE_CAP).ifPresent(cap -> {
                                cap.incrementRageAndTrigger(aggroEntities.size() / 5F, player);
                                PacketHandler.sendToAllClients(new ClientboundSyncCapabilitiesPacket(player.getId(), player));
                            }
                    );
                }
            }
        }
    }
}
