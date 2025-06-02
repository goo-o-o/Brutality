package net.goo.armament.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModUtils {

    public static SimpleParticleType getRandomParticle(List<RegistryObject<SimpleParticleType>> particleType) {
        return particleType.get(random.nextInt(particleType.size())).get();
    }

    public static SoundEvent getRandomSound(List<RegistryObject<SoundEvent>> soundEvent) {
        return soundEvent.get(random.nextInt(soundEvent.size())).get();
    }


    protected static final RandomSource random = RandomSource.create();

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


    public static class ModEasings {
        public static float easeQuadOut(float input) {
            return input * (2.0F - input);
        }

        public static float easeQuadIn(float input) {
            return input * input;
        }
    }

    public static float nextFloatBetweenInclusive(RandomSource random, float min, float max) {
        return min + random.nextFloat() * (max - min);
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
