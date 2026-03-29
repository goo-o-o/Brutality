package net.goo.brutality.util.lightning;

import net.goo.brutality.client.particle.base.ChainLightningParticle;
import net.goo.brutality.client.particle.providers.ChainLightningParticleData;
import net.goo.brutality.common.item.generic.augments.BrutalitySealAugmentItem;
import net.goo.brutality.common.network.PacketHandler;
import net.goo.brutality.common.network.clientbound.ClientboundChainLightningPacket;
import net.goo.brutality.event.forge.DelayedTaskScheduler;
import net.goo.brutality.util.AugmentHelper;
import net.goo.brutality.util.math.PhysicsUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;

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

    public enum LightningType {
        MAX(ChainLightningParticleData.BoltRenderInfo.DEFAULT.color(MAX_COLORS).noise(0.65F, 0.2F).branching(0.15F, 0.1F));

        public final ChainLightningParticleData.BoltRenderInfo renderInfo;

        LightningType(ChainLightningParticleData.BoltRenderInfo renderInfo) {
            this.renderInfo = renderInfo;
        }
    }

    public static void chainLightning(LivingEntity attacker, ItemStack weapon, LivingEntity origin, int quota, float radius, float maxDamage, float particleSize, int lifespan, LightningType lightningType) {
        Vec3 position = origin.getRopeHoldPosition(0);
        Level level = origin.level();

        int modifiedQuota = quota;
        LivingEntity current = origin;
        Set<LivingEntity> hitMobs = new HashSet<>();
        while (modifiedQuota > 0) {
            LivingEntity closestEntity = level.getNearestEntity(LivingEntity.class,
                    TargetingConditions.DEFAULT.ignoreLineOfSight().selector(e -> !hitMobs.contains(e)), origin, position.x(), position.y(), position.z(),
                    current.getBoundingBox().inflate(radius));
            float amount = maxDamage * ((float) modifiedQuota / quota);

            // Inside while (modifiedQuota > 0)
            BlockPos currentPos = BlockPos.containing(position);
            int staticAttempts = 2; // How many "extra" static bolts to generate per jump

            for (int i = 0; i < staticAttempts; i++) {
                // Search in a small radius for a solid block
                BlockPos randomOffset = currentPos.offset(
                        level.random.nextInt(5) - 2,
                        level.random.nextInt(5) - 2,
                        level.random.nextInt(5) - 2
                );

                if (level.getBlockState(randomOffset).isSolidRender(level, randomOffset)) {
                    // Send a "cosmetic" bolt to this block
                    if (!level.isClientSide()) {
                        PacketHandler.sendToNearbyClients(
                                new ClientboundChainLightningPacket(
                                        position.toVector3f(),
                                        randomOffset.getCenter().toVector3f(),
                                        particleSize * 0.5F, // Static bolts should be thinner
                                        lifespan,         // and shorter lived
                                        lightningType, 1, 0), // No branching for static
                                (ServerLevel) level, currentPos.getX(), currentPos.getY(), currentPos.getZ(), 64
                        );
                    }
                }
            }

            if (closestEntity != null) {
                closestEntity.hurt(closestEntity.damageSources().lightningBolt(), amount);

                hitMobs.add(closestEntity);
                if (!level.isClientSide())
                    PacketHandler.sendToTracking(
                            new ClientboundChainLightningPacket(position.toVector3f(), closestEntity.getRopeHoldPosition(0).toVector3f(), particleSize, lifespan, lightningType, 3, 2),
                            closestEntity);

                position = closestEntity.getRopeHoldPosition(0);

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

    @OnlyIn(Dist.CLIENT)
    public static class Client {
        public static void handlePacket(int iterations, int delay, LightningType lightningType, Vector3f start, Vector3f end, float size, int lifespan) {
            if (Minecraft.getInstance().level != null) {
                for (int i = 0; i < iterations; i++) {
                    DelayedTaskScheduler.queueClientWork(Minecraft.getInstance().level, delay * i, () ->
                            ChainLightningHelper.Client.shock(
                                    lightningType,
                                    PhysicsUtils.fromVector3f(start),
                                    PhysicsUtils.fromVector3f(end),
                                    size,
                                    lifespan));
                }
            }
        }

        public static void shock(ChainLightningHelper.LightningType lightningType, Vec3 vectorStart, Vec3 vectorEnd, float size, int lifespan) {
            if (Minecraft.getInstance().level == null) {
                return;
            }
            ChainLightningParticle.INSTANCE.add(Minecraft.getInstance().level, new ChainLightningParticleData(lightningType.renderInfo, vectorStart, vectorEnd, lifespan).size(size), Minecraft.getInstance().getPartialTick());
        }
    }
}
