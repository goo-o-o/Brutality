package net.goo.brutality.common.item.curios.charm;

import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;

public class SelfRepairNexus extends BrutalityCurioItem {

    public SelfRepairNexus(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    /**
     * Heals nearby players wearing {@link BrutalityItems#SELF_REPAIR_NEXUS} for 1 heart <br>
     * Should be called in a {@link net.minecraftforge.event.entity.living.LivingDeathEvent}
     * @param victim The entity dying
     */
    public static void processNearbyDeath(LivingEntity victim) {
        victim.level().getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, victim, victim.getBoundingBox().inflate(3))
                .forEach(nearbyEntity -> CuriosApi.getCuriosInventory(nearbyEntity).ifPresent(handler -> {
                    if (handler.isEquipped(BrutalityItems.SELF_REPAIR_NEXUS.get())) nearbyEntity.heal(2);
                }));
    }
}
