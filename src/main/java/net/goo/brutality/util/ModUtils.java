package net.goo.brutality.util;

import net.goo.brutality.particle.providers.WaveParticleData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ModUtils {
    protected static final RandomSource random = RandomSource.create();

    public static List<? extends Entity> getEntitiesInSphere(Entity origin, @Nullable Predicate<? super Entity> predicate, double distance) {
        List<? extends Entity> aabbEntities = predicate == null ?
                origin.level().getEntities(origin, origin.getBoundingBox().inflate(distance)) :
                origin.level().getEntities(origin, origin.getBoundingBox().inflate(distance), predicate);
        return aabbEntities.stream().filter(entity -> entity.distanceTo(origin) < distance).toList();
    }

    public static <T extends Entity> List<T> getEntitiesInSphere(Class<T> clazz, Entity origin, Predicate<? super Entity> predicate, double distance) {
        List<T> aabbEntities = predicate == null ?
                origin.level().getEntitiesOfClass(clazz, origin.getBoundingBox().inflate(distance)) :
                origin.level().getEntitiesOfClass(clazz, origin.getBoundingBox().inflate(distance), predicate);
        return aabbEntities.stream().filter(entity -> entity.distanceTo(origin) < distance && entity != origin).toList();
    }

    public static <T extends ParticleOptions> int sendParticles(ServerLevel serverLevel, T pType, boolean longDistance, double pPosX, double pPosY, double pPosZ, int pParticleCount, double pXOffset, double pYOffset, double pZOffset, double pSpeed) {
        ClientboundLevelParticlesPacket clientboundlevelparticlespacket = new ClientboundLevelParticlesPacket(pType, false, pPosX, pPosY, pPosZ, (float) pXOffset, (float) pYOffset, (float) pZOffset, (float) pSpeed, pParticleCount);
        int i = 0;

        for (int j = 0; j < serverLevel.players().size(); ++j) {
            ServerPlayer serverPlayer = serverLevel.players().get(j);
            if (sendParticles(serverLevel, serverPlayer, longDistance, pPosX, pPosY, pPosZ, clientboundlevelparticlespacket)) {
                ++i;
            }
        }

        return i;
    }

    public static <T extends ParticleOptions> int sendParticles(ServerLevel serverLevel, T pType,
                                                                boolean longDistance, Entity toSpawnOn, double pXOffset,
                                                                double pYOffset, double pZOffset, int pParticleCount, double pSpeed) {
        return sendParticles(serverLevel, pType, longDistance, toSpawnOn.getX(), toSpawnOn.getY(0.5), toSpawnOn.getZ(), pParticleCount, pXOffset, pYOffset, pZOffset, pSpeed);
    }

    public static <T extends ParticleOptions> int sendParticles(ServerLevel serverLevel, T pType,
                                                                boolean longDistance, Vec3 spawnPos,
                                                                double pXOffset, double pYOffset, double pZOffset, int pParticleCount, double pSpeed) {
        return sendParticles(serverLevel, pType, longDistance, spawnPos.x, spawnPos.y, spawnPos.z, pParticleCount, pXOffset, pYOffset, pZOffset, pSpeed);
    }

    public static <T extends ParticleOptions> int sendParticles(ServerLevel serverLevel, T pType,
                                                                boolean longDistance, double pPosX, double pPosY, double pPosZ, int pParticleCount, double pSpeed) {
        return sendParticles(serverLevel, pType, longDistance, pPosX, pPosY, pPosZ, pParticleCount, 0, 0, 0, pSpeed);
    }

    private static boolean sendParticles(ServerLevel serverLevel, ServerPlayer pPlayer, boolean pLongDistance, double pPosX, double pPosY, double pPosZ, Packet<?> pPacket) {
        if (pPlayer.level() != serverLevel) {
            return false;
        } else {
            BlockPos blockpos = pPlayer.blockPosition();
            if (blockpos.closerToCenterThan(new Vec3(pPosX, pPosY, pPosZ), pLongDistance ? 512.0D : 32.0D)) {
                pPlayer.connection.send(pPacket);
                return true;
            } else {
                return false;
            }
        }
    }


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

        public static float easeOut(float t) {
            return 1 - (1 - t) * (1 - t);
        }

    }

    private static final ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(1);

    public static <T extends Entity> List<T> applyWaveEffect(ServerLevel level, Vec3 origin, Class<T> clazz, WaveParticleData particleData, @Nullable Predicate<? super T> filter, Consumer<Entity> effect) {
        return applyWaveEffect(level, origin.x(), origin.y(), origin.z(), clazz, particleData, filter, effect);
    }

    public static <T extends Entity> List<T> applyWaveEffect(ServerLevel level, Entity origin, Class<T> clazz, WaveParticleData particleData, @Nullable Predicate<? super T> filter, Consumer<Entity> effect) {
        return applyWaveEffect(level, origin.getX(), origin.getY(0.5), origin.getZ(), clazz, particleData, filter, effect);
    }

    public static <T extends Entity> List<T> applyWaveEffect(ServerLevel level, double x, double y, double z, Class<T> clazz, WaveParticleData particleData, @Nullable Predicate<? super T> filter, Consumer<Entity> effect) {
        float maxRadius = particleData.radius();
        int lifetimeTicks = particleData.growthDuration();

        AABB box = new AABB(
                new Vec3(x - maxRadius, y, z - maxRadius),
                new Vec3(x + maxRadius, y, z + maxRadius));

        List<T> entities = filter != null ? level.getEntitiesOfClass(clazz, box, filter) : level.getEntitiesOfClass(clazz, box);

        entities.forEach(entity -> {
            float distance = Mth.sqrt((float) entity.distanceToSqr(x ,y ,z));

            // Normalize distance (0 to 1)
            float normalizedDistance = distance / maxRadius;
            if (normalizedDistance > 1) return;
            // Use the same easeOut function as the particle
            // Solve for growthProgress where easeOut(growthProgress) = normalizedDistance
            // easeOut(t) = 1 - (1 - t)^2
            // Solve: 1 - (1 - t)^2 = normalizedDistance
            // (1 - t)^2 = 1 - normalizedDistance
            // 1 - t = sqrt(1 - normalizedDistance)
            // t = 1 - sqrt(1 - normalizedDistance)
            float growthProgress = 1.0F - (float) Math.sqrt(1.0F - normalizedDistance);
            // Calculate delay in ticks based on growthProgress
            long delayTicks = (long) (growthProgress * lifetimeTicks);

            // Convert to milliseconds (1 tick = 50ms)
            long delayMillis = delayTicks * 50L;

            // Schedule the effect
            EXECUTOR.schedule(() -> {
                if (entity.isAlive()) {
                    effect.accept(entity);
                }
            }, delayMillis, TimeUnit.MILLISECONDS);
        });

        return entities;
    }


    public static <T extends Entity> RayData<T> getEntitiesInRay(Class<T> entityClass, LivingEntity origin,
                                                                 float maxRange, ClipContext.Fluid fluidContext, ClipContext.Block blockContext,
                                                                 float beamRadius, @Nullable Predicate<? super T> filter, @NotNull Integer pierceCap,
                                                                 @Nullable Consumer<Vec3> segmentConsumer
    ) {

        Vec3 startPos = origin.getEyePosition();
        Vec3 endPos = startPos.add(origin.getLookAngle().scale(maxRange));
        Level level = origin.level();

        BlockHitResult blockResult = level.clip(new ClipContext(
                startPos,
                endPos,
                blockContext,
                fluidContext,
                origin
        ));

        int distance = (int) startPos.distanceTo(blockResult.getBlockPos().getCenter());
        List<T> targets = collectEntitiesAlongRay(entityClass, origin, startPos, distance, beamRadius, filter, pierceCap, segmentConsumer);

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

    public static BlockPos getBlockLookingAt(Player player, boolean isFluid, float hitDistance) {
        HitResult block = player.pick(hitDistance, 1.0F, isFluid);

        if (block.getType() == HitResult.Type.BLOCK) {
            return ((BlockHitResult) block).getBlockPos();
        }
        return null;
    }

    public static Vec3 getRandomPosAroundPlayer(Player player, float scale) {
        double randomX = player.getRandomX(Mth.nextFloat(player.getRandom(), -scale, scale));
        double randomY = player.getRandomY() * Mth.nextFloat(player.getRandom(), -scale, scale);
        double randomZ = player.getRandomZ(Mth.nextFloat(player.getRandom(), -scale, scale));

        return new Vec3(randomX, randomY, randomZ);
    }

    public static boolean isStandable(BlockGetter level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        VoxelShape shape = state.getCollisionShape(level, pos);
        return !shape.isEmpty() && shape.max(Direction.Axis.Y) >= 0.75;
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
            Vec3 entityPos = new Vec3(entity.getX(), entity.getY(0.5), entity.getZ());
            Vec3 directionToEntity = entityPos.subtract(playerPos);
            double distanceToEntity = directionToEntity.length();

            Vec3 endPos = playerPos.add(viewVector.scale(range));

            if (distanceToEntity > 0 && distanceToEntity <= range) {
                directionToEntity = directionToEntity.normalize();
                double dotProduct = viewVector.dot(directionToEntity);

                // Check if the angle is within range
                if (dotProduct > 1.0D - 0.075D / distanceToEntity) { // Adjust threshold as needed
                    // Check line of sight with raycasting
                    ClipContext context = new ClipContext(playerPos, entityPos, ClipContext.Block.COLLIDER, net.minecraft.world.level.ClipContext.Fluid.NONE, pPlayer);
                    BlockHitResult blockHit = pPlayer.level().clip(context);

                    if (blockHit.getType() == BlockHitResult.Type.MISS) {
                        // Check hitbox intersection
                        Optional<Vec3> clipPoint = entity.getBoundingBox().clip(playerPos, endPos);
                        if (clipPoint.isPresent()) {
                            double distance = playerPos.distanceTo(clipPoint.get());
                            if (distance < closestDistance) {
                                closestEntity = entity;
                                closestDistance = distance;
                            }
                        }
                    }

                }
            }
        }

        return closestEntity; // Return the closest entity or null if none found
    }

}
