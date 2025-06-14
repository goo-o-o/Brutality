package net.goo.brutality.entity.mobs;

import net.goo.brutality.entity.capabilities.GenericCopyOwnerTargetGoal;
import net.goo.brutality.entity.capabilities.GenericHurtByTargetGoal;
import net.goo.brutality.entity.capabilities.GenericOwnerHurtByTargetGoal;
import net.goo.brutality.entity.capabilities.GenericOwnerHurtTargetGoal;
import net.goo.brutality.entity.capabilities.goals.GenericFollowOwnerGoal;
import net.goo.brutality.registry.ModEntities;
import net.goo.brutality.util.helpers.OwnerHelper;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.UUID;

public class SummonedStray extends Stray implements ISummon {
    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;
    public SummonedStray(EntityType<? extends Stray> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        xpReward = 0;
    }

    public SummonedStray(Level level, LivingEntity owner, boolean playRiseAnimation) {
        this(ModEntities.SUMMONED_STRAY.get(), level);
        setSummoner(owner);
    }


    @Override
    public void registerGoals() {

        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(7, new GenericFollowOwnerGoal(this, this::getSummoner, 0.9f, 15, 5, false, 25));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));

        this.targetSelector.addGoal(1, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(2, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, (new GenericHurtByTargetGoal(this, (entity) -> entity == getSummoner())).setAlertOthers());

    }

    @Override
    public boolean isPreventingPlayerRest(Player pPlayer) {
        return !this.isAlliedTo(pPlayer);
    }

    @Override
    public LivingEntity getSummoner() {
        return OwnerHelper.getAndCacheOwner(level(), cachedSummoner, summonerUUID);
    }

    public void setSummoner(@Nullable LivingEntity owner) {
        if (owner != null) {
            this.summonerUUID = owner.getUUID();
            this.cachedSummoner = owner;
        }
    }

    @Override
    public void die(DamageSource pDamageSource) {
        this.onDeathHelper();
        super.die(pDamageSource);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.summonerUUID = OwnerHelper.deserializeOwner(compoundTag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        OwnerHelper.serializeOwner(compoundTag, summonerUUID);
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        return super.isAlliedTo(pEntity) || this.isAlliedHelper(pEntity);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && shouldIgnoreDamage(pSource)) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }


    @Override
    public void onUnSummon() {
        if (level() instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.POOF, getX(), getY(), getZ(), 25, .4, .8, .4, .03);
            discard();
        }
    }

    @Override
    protected boolean isSunBurnTick() {
        return false;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        RandomSource randomsource = level().random;
        this.populateDefaultEquipmentSlots(randomsource, pDifficulty);
        if (randomsource.nextDouble() < .3)
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));

        this.reassessWeaponGoal();

        return pSpawnData;
    }


    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    //
    //  Rise Animation Stuff
    //
    //

    protected void clientDiggingParticles(LivingEntity livingEntity) {
        RandomSource randomsource = livingEntity.getRandom();
        BlockState blockstate = livingEntity.getBlockStateOn();
        if (blockstate.getRenderShape() != RenderShape.INVISIBLE) {
            for (int i = 0; i < 15; ++i) {
                double d0 = livingEntity.getX() + (double) Mth.randomBetween(randomsource, -0.5F, 0.5F);
                double d1 = livingEntity.getY();
                double d2 = livingEntity.getZ() + (double) Mth.randomBetween(randomsource, -0.5F, 0.5F);
                livingEntity.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate), d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}