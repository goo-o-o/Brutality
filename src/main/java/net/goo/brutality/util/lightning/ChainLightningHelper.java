package net.goo.brutality.util.lightning;

import net.goo.brutality.client.particle.base.ChainLightningParticle;
import net.goo.brutality.client.particle.providers.ChainLightningParticleData;
import net.goo.brutality.common.item.generic.augments.BrutalitySealAugmentItem;
import net.goo.brutality.common.network.PacketHandler;
import net.goo.brutality.common.network.clientbound.ClientboundChainLightningPacket;
import net.goo.brutality.util.AugmentHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class ChainLightningHelper {
    private static final Color[] MAX_COLORS = new Color[]{
            new Color(0, 131, 255),
            new Color(0, 210, 171),
            new Color(255, 241, 0),
            new Color(255, 34, 0)
    };
    private static final Color[] THUNDERBOLT_COLORS = new Color[]{
            new Color(234, 219, 132),

    };

    public enum LightningType {
        MAX(ChainLightningParticleData.BoltRenderInfo.DEFAULT.color(MAX_COLORS).noise(0.65F, 0.2F).branching(0.15F, 0.1F)),
        THUNDERBOLT(ChainLightningParticleData.BoltRenderInfo.thunderBolt(new Color(255, 241, 0)));

        public final ChainLightningParticleData.BoltRenderInfo renderInfo;

        LightningType(ChainLightningParticleData.BoltRenderInfo renderInfo) {
            this.renderInfo = renderInfo;
        }
    }

    private static Vec3 getRandomPos(Entity entity, float scale) {
        return new Vec3(entity.getRandomX(scale), entity.getY(entity.random.nextFloat() * scale), entity.getRandomZ(scale));
    }


    /**
     * Refactored Server-side chain lightning.
     */
    public static void chainLightning(LivingEntity attacker, ItemStack weapon, LivingEntity origin, int quota, float radius, float maxDamage, float particleSize, int lifespan, LightningType lightningType) {
        Vec3 position = getRandomPos(origin, origin.getBbHeight() * 0.75F);
        Level level = origin.level();

        int modifiedQuota = quota;
        LivingEntity current = origin;
        Set<LivingEntity> hitMobs = new HashSet<>();

        while (modifiedQuota > 0) {
            LivingEntity closestEntity = level.getNearestEntity(LivingEntity.class,
                    TargetingConditions.DEFAULT.ignoreLineOfSight().selector(e -> !hitMobs.contains(e)), origin, position.x(), position.y(), position.z(),
                    current.getBoundingBox().inflate(radius));

            float amount = maxDamage * ((float) modifiedQuota / quota);

            // Logic for the ground-connection "static" look
            if (!level.isClientSide()) {
                sendStaticPackets((ServerLevel) level, position, particleSize, lifespan, lightningType);
            }

            if (closestEntity != null) {
                closestEntity.hurt(closestEntity.damageSources().lightningBolt(), amount);
                hitMobs.add(closestEntity);

                Vec3 nextPos = getRandomPos(closestEntity, closestEntity.getBbHeight() * 0.75F);

                if (!level.isClientSide()) {
                    PacketHandler.sendToTracking(
                            new ClientboundChainLightningPacket(position.toVector3f(), nextPos.toVector3f(), particleSize, lifespan, lightningType, 3, 2),
                            closestEntity);
                }

                position = nextPos;

                AugmentHelper.getAugmentCounts(weapon).forEach((brutalityAugmentItem, integer) -> {
                    if (brutalityAugmentItem instanceof BrutalitySealAugmentItem sealAugmentItem) {
                        sealAugmentItem.onHurtEntity(attacker, closestEntity, amount, integer);
                    }
                });

                modifiedQuota--;
                current = closestEntity;
                continue;
            }
            break;
        }
    }

    /**
     * Reusable logic for sending static bolt packets from the server.
     */
    private static void sendStaticPackets(ServerLevel level, Vec3 pos, float size, int life, LightningType type) {
        BlockPos center = BlockPos.containing(pos);
        for (int i = 0; i < 2; i++) {
            BlockPos offset = center.offset(level.random.nextInt(5) - 2, level.random.nextInt(5) - 2, level.random.nextInt(5) - 2);
            if (level.getBlockState(offset).isSolidRender(level, offset)) {
                PacketHandler.sendToNearbyClients(
                        new ClientboundChainLightningPacket(pos.toVector3f(), offset.getCenter().toVector3f(), size * 0.5F, life, type, 1, 0),
                        level, center.getX(), center.getY(), center.getZ(), 64
                );
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Client {
        public static void shock(ClientLevel clientLevel, ChainLightningHelper.LightningType lightningType, Vec3 vectorStart, Vec3 vectorEnd, float size, int lifespan) {
            ChainLightningParticle.INSTANCE.add(clientLevel, new ChainLightningParticleData(lightningType.renderInfo, vectorStart, vectorEnd, lifespan).size(size), Minecraft.getInstance().getPartialTick());
        }

        /**
         * Spawn bolts from the entity to nearby solid blocks, if not just randomly discharge
         *
         * @param entity The entity to center the effects on.
         * @param type   The LightningType to use.
         * @param count  How many bolts to spawn this call.
         * @param range  How far to look for blocks (radius).
         */
        public static void visualStaticArc(Entity entity, LightningType type, int count, float range, float size, int lifespan) {
            if (entity == null || !entity.level().isClientSide()) return;

            ClientLevel level = (ClientLevel) entity.level();
            Vec3 origin = getRandomPos(entity, entity.getBbHeight() * 0.75F);
            BlockPos currentPos = BlockPos.containing(origin);

            for (int i = 0; i < count; i++) {
                // Find a random block nearby
                BlockPos targetBlock = currentPos.offset(
                        level.random.nextInt((int) (range * 2)) - (int) range,
                        level.random.nextInt((int) (range * 2)) - (int) range,
                        level.random.nextInt((int) (range * 2)) - (int) range
                );

                if (level.getBlockState(targetBlock).isSolidRender(level, targetBlock)) {
                    // Spawn the particle directly using the Client class
                    Client.shock(level, type, origin, targetBlock.getCenter(), size, lifespan);
                } else {
                    // Fallback: If no block found, just arc to a random point in the air
                    // to simulate "discharge"
                    Vec3 dischargePos = origin.add(
                            (level.random.nextFloat() - 0.5) * range,
                            (level.random.nextFloat() - 0.5) * range,
                            (level.random.nextFloat() - 0.5) * range
                    );
                    Client.shock(level, type, origin, dischargePos, size * 0.7F, lifespan / 2);
                }
            }
        }

    }
}
