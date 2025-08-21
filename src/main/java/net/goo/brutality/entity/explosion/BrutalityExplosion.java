package net.goo.brutality.entity.explosion;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class BrutalityExplosion extends Explosion {
    private final Map<Entity, Vec3> hitEntities = Maps.newHashMap();
    public float damageScale = 1;


    public BrutalityExplosion(Level pLevel, @Nullable Entity pSource, double pToBlowX, double pToBlowY, double pToBlowZ, float pRadius, boolean pFire, BlockInteraction pBlockInteraction) {
        super(pLevel, pSource, pToBlowX, pToBlowY, pToBlowZ, pRadius, pFire, pBlockInteraction);
    }

    public BrutalityExplosion(Level pLevel, @Nullable Entity pSource, @Nullable DamageSource pDamageSource, @Nullable ExplosionDamageCalculator pDamageCalculator, double pToBlowX, double pToBlowY, double pToBlowZ, float pRadius, boolean pFire, BlockInteraction pBlockInteraction) {
        super(pLevel, pSource, pDamageSource, pDamageCalculator, pToBlowX, pToBlowY, pToBlowZ, pRadius, pFire, pBlockInteraction);
    }

    protected SimpleParticleType getParticleEmitter() {
        return ParticleTypes.EXPLOSION_EMITTER;
    }

    protected SimpleParticleType getParticle() {
        return ParticleTypes.EXPLOSION;
    }

    protected SoundEvent getExplosionSound() {
        return SoundEvents.GENERIC_EXPLODE;
    }

    public Map<Entity, Vec3> getHitEntities() {
        return hitEntities;
    }

    protected boolean needsInteractWithBlocksForEmitter() {
        return true;
    }

    public Predicate<Entity> getEntityFilter() {
        return entity -> true;
    }

    @Override
    public void finalizeExplosion(boolean pSpawnParticles) {
        if (this.level.isClientSide) {
            this.level.playLocalSound(this.x, this.y, this.z, getExplosionSound(), SoundSource.BLOCKS, 4.0F, (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F, false);
        }

        boolean interactsWithBlocks = this.interactsWithBlocks();
        if (pSpawnParticles) {
            if (needsInteractWithBlocksForEmitter()) {
                if (!(this.radius < 2.0F) && interactsWithBlocks) {
                    this.level.addParticle(getParticleEmitter(), this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
                } else {
                    this.level.addParticle(getParticle(), this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
                }
            } else {
                if (!(this.radius < 2.0F)) {
                    this.level.addParticle(getParticleEmitter(), this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
                } else {
                    this.level.addParticle(getParticle(), this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
                }
            }
        }

        if (interactsWithBlocks) {
            ObjectArrayList<Pair<ItemStack, BlockPos>> objectarraylist = new ObjectArrayList<>();
            boolean flag1 = this.getIndirectSourceEntity() instanceof Player;
            Util.shuffle(this.toBlow, this.level.random);

            for (BlockPos blockpos : this.getToBlow()) {
                BlockState blockstate = this.level.getBlockState(blockpos);
                if (!blockstate.isAir()) {
                    BlockPos itemDropPos = blockpos.immutable();
                    this.level.getProfiler().push("explosion_blocks");
                    if (blockstate.canDropFromExplosion(this.level, blockpos, this)) {
                        if (this.level instanceof ServerLevel serverlevel) {
                            BlockEntity blockentity = blockstate.hasBlockEntity() ? this.level.getBlockEntity(blockpos) : null;
                            LootParams.Builder lootparams$builder = (new LootParams.Builder(serverlevel)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockpos)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockentity).withOptionalParameter(LootContextParams.THIS_ENTITY, this.source);
                            if (this.blockInteraction == Explosion.BlockInteraction.DESTROY_WITH_DECAY) {
                                lootparams$builder.withParameter(LootContextParams.EXPLOSION_RADIUS, this.radius);
                            }

                            blockstate.spawnAfterBreak(serverlevel, blockpos, ItemStack.EMPTY, flag1);
                            blockstate.getDrops(lootparams$builder).forEach((p_46074_) -> {
                                addBlockDrops(objectarraylist, p_46074_, itemDropPos);
                            });
                        }
                    }

                    blockstate.onBlockExploded(this.level, blockpos, this);
                    this.level.getProfiler().pop();
                }
            }

            for (Pair<ItemStack, BlockPos> pair : objectarraylist) {
                Block.popResource(this.level, pair.getSecond(), pair.getFirst());
            }
        }

        if (this.fire) {
            for (BlockPos blowPos : this.getToBlow()) {
                if (this.random.nextInt(3) == 0 && this.level.getBlockState(blowPos).isAir() && this.level.getBlockState(blowPos.below()).isSolidRender(this.level, blowPos.below())) {
                    this.level.setBlockAndUpdate(blowPos, BaseFireBlock.getState(this.level, blowPos));
                }
            }
        }

    }

    @Override
    public void explode() {
        this.level.gameEvent(this.source, GameEvent.EXPLODE, new Vec3(this.x, this.y, this.z));
        Set<BlockPos> affectedBlocks = Sets.newHashSet();
        int rayCount = 16;

        for (int rayX = 0; rayX < rayCount; ++rayX) {
            for (int rayY = 0; rayY < rayCount; ++rayY) {
                for (int rayZ = 0; rayZ < rayCount; ++rayZ) {
                    if (rayX == 0 || rayX == 15 || rayY == 0 || rayY == 15 || rayZ == 0 || rayZ == 15) {
                        double directionX = (float) rayX / 15.0F * 2.0F - 1.0F;
                        double directionY = (float) rayY / 15.0F * 2.0F - 1.0F;
                        double directionZ = (float) rayZ / 15.0F * 2.0F - 1.0F;
                        double magnitude = Math.sqrt(directionX * directionX + directionY * directionY + directionZ * directionZ);
                        directionX /= magnitude;
                        directionY /= magnitude;
                        directionZ /= magnitude;
                        float rayStrength = this.radius * (0.7F + this.level.random.nextFloat() * 0.6F);
                        double currentX = this.x;
                        double currentY = this.y;
                        double currentZ = this.z;

                        for (float stepSize = 0.3F; rayStrength > 0.0F; rayStrength -= 0.22500001F) {
                            BlockPos currentPos = BlockPos.containing(currentX, currentY, currentZ);
                            BlockState blockState = this.level.getBlockState(currentPos);
                            FluidState fluidState = this.level.getFluidState(currentPos);
                            if (!this.level.isInWorldBounds(currentPos)) {
                                break;
                            }

                            Optional<Float> explosionResistance = this.damageCalculator.getBlockExplosionResistance(this, this.level, currentPos, blockState, fluidState);
                            if (explosionResistance.isPresent()) {
                                rayStrength -= (explosionResistance.get() + 0.3F) * 0.3F;
                            }

                            if (rayStrength > 0.0F && this.damageCalculator.shouldBlockExplode(this, this.level, currentPos, blockState, rayStrength)) {
                                affectedBlocks.add(currentPos);
                            }

                            currentX += directionX * (double) 0.3F;
                            currentY += directionY * (double) 0.3F;
                            currentZ += directionZ * (double) 0.3F;
                        }
                    }
                }
            }
        }

        this.toBlow.addAll(affectedBlocks);
        float explosionDiameter = this.radius * 2.0F;
        int minX = Mth.floor(this.x - (double) explosionDiameter - (double) 1.0F);
        int maxX = Mth.floor(this.x + (double) explosionDiameter + (double) 1.0F);
        int minY = Mth.floor(this.y - (double) explosionDiameter - (double) 1.0F);
        int maxY = Mth.floor(this.y + (double) explosionDiameter + (double) 1.0F);
        int minZ = Mth.floor(this.z - (double) explosionDiameter - (double) 1.0F);
        int maxZ = Mth.floor(this.z + (double) explosionDiameter + (double) 1.0F);

        List<Entity> nearbyEntities = this.level.getEntities(this.source, new AABB(minX, minY, minZ, maxX, maxY, maxZ));
        ForgeEventFactory.onExplosionDetonate(this.level, this, nearbyEntities, explosionDiameter);
        Vec3 explosionCenter = new Vec3(this.x, this.y, this.z);

        for (Entity entity : nearbyEntities) {
            if (!entity.ignoreExplosion() && getEntityFilter().test(entity)) {
                double distanceRatio = Math.sqrt(entity.distanceToSqr(explosionCenter)) / (double) explosionDiameter;
                if (distanceRatio <= (double) 1.0F) {

                    double deltaX = entity.getX() - this.x;
                    double deltaY = (entity instanceof PrimedTnt ? entity.getY() : entity.getEyeY()) - this.y;
                    double deltaZ = entity.getZ() - this.z;
                    double totalDistance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

                    if (totalDistance != (double) 0.0F) {

                        deltaX /= totalDistance;
                        deltaY /= totalDistance;
                        deltaZ /= totalDistance;

                        double visibilityFactor = getSeenPercent(explosionCenter, entity);
                        double impactFactor = ((double) 1.0F - distanceRatio) * visibilityFactor;

                        entity.hurt(this.getDamageSource(), damageScale * (float) ((impactFactor * impactFactor + impactFactor) / 2.0F * 7.0F * explosionDiameter + 1.0F));
                        onHit(entity, impactFactor);
                        double knockbackFactor;
                        if (entity instanceof LivingEntity livingEntity) {
                            knockbackFactor = ProtectionEnchantment.getExplosionKnockbackAfterDampener(livingEntity, impactFactor);
                        } else {
                            knockbackFactor = impactFactor;
                        }

                        deltaX *= knockbackFactor;
                        deltaY *= knockbackFactor;
                        deltaZ *= knockbackFactor;

                        Vec3 knockbackVector = new Vec3(deltaX, deltaY, deltaZ);
                        entity.setDeltaMovement(entity.getDeltaMovement().add(knockbackVector));

                        if (entity instanceof Player player) {
                            if (!player.isSpectator() && (!player.isCreative() || !player.getAbilities().flying)) {
                                this.hitPlayers.put(player, knockbackVector);
                            }
                        } else {
                            this.hitEntities.put(entity, knockbackVector);
                        }
                    }
                }
            }
        }
    }

    public void onHit(Entity entity, double impactFactor) {
    }
}