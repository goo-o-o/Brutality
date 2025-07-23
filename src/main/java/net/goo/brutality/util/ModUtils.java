package net.goo.brutality.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ModUtils {
    protected static final RandomSource random = RandomSource.create();


    public record RayData<T extends Entity>(List<T> entityList, int distance, Vec3 endPos) {
        public RayData(List<T> entityList, int distance, Vec3 endPos) {
            this.entityList = Collections.unmodifiableList(entityList);
            this.distance = distance;
            this.endPos = endPos;
        }
    }

    public static class ModEasings {
        public static float easeQuadOut(float input) {
            return input * (2.0F - input);
        }

        public static float easeQuadIn(float input) {
            return input * input;
        }
    }


    public static <T extends Entity> RayData<T> getEntitiesInRay(Class<T> entityClass, LivingEntity livingEntity,
                                                                 float maxRange, ClipContext.Fluid fluidContext, ClipContext.Block blockContext,
                                                                 float beamRadius, @Nullable Predicate<? super T> filter, @NotNull Integer pierceCap,
                                                                 @Nullable Consumer<Vec3> segmentConsumer
    ) {

        Vec3 startPos = livingEntity.getEyePosition();
        Vec3 endPos = startPos.add(livingEntity.getLookAngle().scale(maxRange));
        Level level = livingEntity.level();

        BlockHitResult blockResult = level.clip(new ClipContext(
                startPos,
                endPos,
                blockContext,
                fluidContext,
                livingEntity
        ));

        int distance = (int) startPos.distanceTo(blockResult.getBlockPos().getCenter());
        List<T> targets = collectEntitiesAlongRay(entityClass, livingEntity, startPos, distance, beamRadius, filter, pierceCap, segmentConsumer);

        return new RayData<>(targets.stream().distinct().collect(Collectors.toList()), distance, blockResult.getLocation());
    }

    private static <T extends Entity> List<T> collectEntitiesAlongRay(Class<T> entityClass, LivingEntity livingEntity,
                                                                      Vec3 startPos, int distance, float beamRadius, @Nullable Predicate<? super T> filter, Integer pierceCap, @Nullable Consumer<Vec3> segmentConsumer) {

        List<T> targets = new ArrayList<>();
        Vec3 lookAngle = livingEntity.getLookAngle();
        Level level = livingEntity.level();

        if (filter != null) {
            for (float dist = 0; dist < distance; dist += beamRadius * 2) {
                Vec3 segmentCenter = startPos.add(lookAngle.scale(dist));
                AABB segmentBox = new AABB(segmentCenter, segmentCenter).inflate(beamRadius);


                if (segmentConsumer != null) {
                    segmentConsumer.accept(segmentCenter);
                }

                targets.addAll(level.getEntitiesOfClass(entityClass, segmentBox, filter));

                if (targets.size() > pierceCap) break;
            }

        } else {
            for (float dist = 0; dist < distance; dist += beamRadius * 2) {
                Vec3 segmentCenter = startPos.add(lookAngle.scale(dist));
                AABB segmentBox = new AABB(segmentCenter, segmentCenter).inflate(beamRadius);

                targets.addAll(level.getEntitiesOfClass(entityClass, segmentBox));
                if (targets.size() > pierceCap) break;
            }
        }
        return targets;
    }


    public static SimpleParticleType getRandomParticle(List<RegistryObject<SimpleParticleType>> particleType) {
        return particleType.get(random.nextInt(particleType.size())).get();
    }

    public static SoundEvent getRandomSound(List<RegistryObject<SoundEvent>> soundEvent) {
        return soundEvent.get(random.nextInt(soundEvent.size())).get();
    }

    public static SoundEvent getRandomSound(SoundEvent... soundEvent) {
        return soundEvent[random.nextInt(soundEvent.length)];
    }


    public static boolean isPlayerBehind(Player player, Entity entity, int range) {
        Vec3 playerPos = player.getPosition(1.0F);
        Vec3 entityPos = entity.getPosition(1.0F);

        Vec3 targetVec = entityPos.subtract(playerPos);
        float targetDeg = (float) Math.toDegrees(Math.atan2(targetVec.x, targetVec.z)) + 180;

        return targetDeg > (90 - range) && targetDeg < (270 + range);
    }

    public static boolean hasFullArmorSet(LivingEntity livingEntity, ArmorMaterial material) {
        for (ItemStack stack : livingEntity.getArmorSlots()) {
            if (!(stack.getItem() instanceof ArmorItem armorItem)) {
                return false;
            }
            if (armorItem.getMaterial() != material) {
                return false;
            }
        }
        return true;
    }

    public static void setTextureIdx(ItemStack stack, int data) {
        stack.getOrCreateTag().putInt("texture", data);
    }
    public static int getTextureIdx(ItemStack stack) {
        return stack.getOrCreateTag().getInt("texture");
    }

    public static boolean tryExtendEffect(Player player, MobEffect mobEffect, int durationInc) {
        if (player.hasEffect(mobEffect)) {
            MobEffectInstance originalInstance = player.getEffect(mobEffect);
            assert originalInstance != null;
            player.addEffect(new MobEffectInstance(mobEffect,
                    originalInstance.getDuration() + durationInc,
                    originalInstance.getAmplifier(),
                    originalInstance.isAmbient(),
                    originalInstance.isVisible()));
            return true;
        }
        return false;
    }

    public static BlockPos lookingAtBlock(Player player, boolean isFluid, float hitDistance) {
        HitResult block = player.pick(hitDistance, 1.0F, isFluid);

        if (block.getType() == HitResult.Type.BLOCK) {
            return ((BlockHitResult) block).getBlockPos();
        }
        return null;
    }


    public static Entity getEntityPlayerLookingAt(Player pPlayer, double range) {
        // Get player's eye position
        Vec3 playerPos = pPlayer.getEyePosition(1.0F);
        Vec3 viewVector = pPlayer.getViewVector(1.0F).normalize();

        // Get the list of entities within the specified range
        List<Entity> entities = pPlayer.level().getEntitiesOfClass(Entity.class, new AABB(playerPos, playerPos).inflate(range));
        Entity closestEntity = null;
        double closestDistance = Double.MAX_VALUE; // Start with a very large distance

        for (Entity entity : entities) {
            // Calculate direction to the entity
            Vec3 entityPos = new Vec3(entity.getX(), entity.getEyeY(), entity.getZ());
            Vec3 directionToEntity = entityPos.subtract(playerPos);
            double distanceToEntity = directionToEntity.length();

            if (distanceToEntity > 0) {
                directionToEntity = directionToEntity.normalize();
                double dotProduct = viewVector.dot(directionToEntity);

                // Check if the angle is within range
                if (dotProduct > 1.0D - 0.075D / distanceToEntity) { // Adjust threshold as needed
                    // Check line of sight
                    if (pPlayer.hasLineOfSight(entity)) {
                        // If this entity is closer than the closest found so far, update
                        if (distanceToEntity < closestDistance) {
                            closestEntity = entity;
                            closestDistance = distanceToEntity; // Update the closest distance
                        }
                    }
                }
            }
        }
        return closestEntity; // Return the closest entity or null if none found
    }
}
