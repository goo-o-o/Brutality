package net.goo.brutality.util.lightning;

import net.goo.brutality.client.particle.base.ChainLightningParticle;
import net.goo.brutality.client.particle.providers.ChainLightningParticleData;
import net.goo.brutality.common.item.generic.augments.BrutalitySealAugmentItem;
import net.goo.brutality.common.network.PacketHandler;
import net.goo.brutality.common.network.clientbound.ClientboundChainLightningPacket;
import net.goo.brutality.util.AugmentHelper;
import net.minecraft.client.Minecraft;
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

    public enum LightningType {
        MAX(ChainLightningParticleData.BoltRenderInfo.DEFAULT.color(MAX_COLORS).noise(0.2F, 0.6F).branching(0.25F, 0.15F));

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
        public static void shock(ChainLightningHelper.LightningType lightningType, Vec3 vectorStart, Vec3 vectorEnd, float size, int lifespan) {
            if (Minecraft.getInstance().level == null) {
                return;
            }
            ChainLightningParticle.INSTANCE.add(Minecraft.getInstance().level, new ChainLightningParticleData(lightningType.renderInfo, vectorStart, vectorEnd, lifespan).size(size), Minecraft.getInstance().getPartialTick());
        }
    }
}
