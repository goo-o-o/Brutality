package net.goo.brutality.common.item.curios.feet;

import net.goo.brutality.common.entity.explosion.NapalmExplosion;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.util.ModExplosionHelper;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FlameStomper extends BrutalityCurioItem {
    private final Map<UUID, Vec3> lastExplosionMap = new ConcurrentHashMap<>();

    public FlameStomper(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }


    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        lastExplosionMap.put(slotContext.entity().getUUID(), slotContext.entity().getPosition(0));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity livingEntity = slotContext.entity();
        if (livingEntity.level().isClientSide())
            return;

        UUID uuid = livingEntity.getUUID();
        Vec3 currentPos = livingEntity.getPosition(0);
        Vec3 previousPos = lastExplosionMap.getOrDefault(uuid, currentPos);
        if (currentPos.distanceToSqr(previousPos) > 1) {
//            Vec3 behind = livingEntity.getDeltaMovement().normalize().scale(-2).add(currentPos);
            RandomSource random = livingEntity.getRandom();
            NapalmExplosion explosion = new NapalmExplosion(livingEntity.level(), livingEntity, null, null, previousPos.x + random.nextFloat(), previousPos.y + random.nextFloat() + 1, previousPos.z + random.nextFloat(), 2, false, Level.ExplosionInteraction.NONE);
            explosion.setEntityFilter(e -> e != livingEntity);
            ModExplosionHelper.Server.explode(explosion, livingEntity.level(), true);
            lastExplosionMap.put(uuid, currentPos);
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        lastExplosionMap.remove(slotContext.entity().getUUID());
    }
}
