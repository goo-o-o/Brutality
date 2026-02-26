package net.goo.brutality.common.item.curios.feet;

import net.goo.brutality.common.entity.explosion.NapalmExplosion;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.util.ModExplosionHelper;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
    public FlameStomper(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }


    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        LivingEntity livingEntity = slotContext.entity();
        lastExplosionMap.put(livingEntity.getUUID(), new ExplosionData(livingEntity.getPosition(0), livingEntity.level().getGameTime()));
    }

    private final Map<UUID, ExplosionData> lastExplosionMap = new ConcurrentHashMap<>();

    // Small record to hold our tracking data
    private record ExplosionData(Vec3 pos, long time) {
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (entity.level().isClientSide()) return;

        if (entity.tickCount % 2 == 0)
            entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 10, 0));

        UUID uuid = entity.getUUID();
        Vec3 currentPos = entity.position();
        long currentTime = System.currentTimeMillis();

        // 1. Get previous data or initialize if missing
        ExplosionData data = lastExplosionMap.get(uuid);
        if (data == null) {
            lastExplosionMap.put(uuid, new ExplosionData(currentPos, currentTime));
            return;
        }

        // 2. Distance check (1.0 block squared)
        double distSqr = currentPos.distanceToSqr(data.pos());
        if (distSqr >= 4.0) {
            double distance = Math.sqrt(distSqr);
            long timeDiffMs = currentTime - data.time();

            // Prevent division by zero and absurdly high speeds (teleportation)
            float blocksPerSecond = 0;
            if (timeDiffMs > 0) {
                // formula: (distance / ms) * 1000 = blocks per second
                blocksPerSecond = (float) ((distance / timeDiffMs) * 1000.0);
            }


            RandomSource random = entity.getRandom();
            NapalmExplosion explosion = new NapalmExplosion(
                    entity.level(), entity, null, null,
                    data.pos().x + (random.nextFloat() - 0.5),
                    data.pos().y + 0.5,
                    data.pos().z + (random.nextFloat() - 0.5),
                    blocksPerSecond * 0.25F, false, Level.ExplosionInteraction.NONE
            );

            explosion.setDamageFilter(e -> e != entity);
            explosion.knockbackFilter = e -> e != entity;
            ModExplosionHelper.Server.explode(explosion, entity.level(), true);

            // 5. Update the Map with NEW anchor position and NEW time
            lastExplosionMap.put(uuid, new ExplosionData(currentPos, currentTime));
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        lastExplosionMap.remove(slotContext.entity().getUUID());
    }
}
