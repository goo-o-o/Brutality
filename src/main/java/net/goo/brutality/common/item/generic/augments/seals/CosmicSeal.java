package net.goo.brutality.common.item.generic.augments.seals;

import net.goo.brutality.common.entity.spells.cosmic.StarStreamEntity;
import net.goo.brutality.common.item.BrutalityCategories;
import net.goo.brutality.common.item.generic.augments.BrutalitySealAugmentItem;
import net.goo.brutality.common.registry.BrutalityEntities;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CosmicSeal extends BrutalitySealAugmentItem {
    public CosmicSeal(Properties pProperties, BrutalityCategories... itemTypes) {
        super(pProperties, itemTypes);
    }

    private void summonStar(Level level, Vec3 targetPos, LivingEntity owner, int intensity) {
        RandomSource random = level.getRandom();
        StarStreamEntity spellEntity = new StarStreamEntity(BrutalityEntities.STAR_STREAM_ENTITY.get(), level);
        spellEntity.setSpellLevel(intensity);

        Vec3 randomPos = new Vec3(
                targetPos.x + Mth.nextFloat(random, -2, 2),
                targetPos.y + Mth.nextFloat(random, 7.5F, 12.5F),
                targetPos.z + Mth.nextFloat(random, -2, 2)
        );
        spellEntity.setPos(randomPos);
        spellEntity.setOwner(owner);

        level.addFreshEntity(spellEntity);
        level.playSound(null, targetPos.x, targetPos.y, targetPos.z,
                BrutalitySounds.BASS_BOP.get(), SoundSource.AMBIENT,
                1.5F, Mth.nextFloat(random, 0.7F, 1.2F));
        Vec3 direction = targetPos.subtract(randomPos).normalize();
        spellEntity.shoot(direction.x, direction.y, direction.z, 1.5F, 1.5F);
    }

    @Override
    public void onHurt(LivingEntity victim, DamageSource source, float damage, int intensity) {
        Level level = victim.level();
        TargetingConditions conditions = TargetingConditions.DEFAULT.ignoreLineOfSight().selector(entity -> !entity.isAlliedTo(victim) && entity instanceof Monster monster && monster.getTarget() == victim);
        LivingEntity nearest = level.getNearestEntity(LivingEntity.class, conditions, victim, victim.getX(), victim.getY(0.5), victim.getZ(), victim.getBoundingBox().inflate(5));

        if (nearest == null) return;
        Vec3 targetPos = nearest.getPosition(1).add(0, nearest.getBbHeight() * 0.5, 0);
        summonStar(level, targetPos, victim, intensity);
    }

    @Override
    public void onHurtByEntity(Entity attacker, LivingEntity victim, DamageSource source, float damage, int intensity) {
        summonStar(attacker.level(), attacker.getPosition(0).add(0, attacker.getBbHeight() * 0.5, 0), victim, intensity);
    }

    @Override
    public void onProcAtLocation(LivingEntity attacker, Vec3 location, int intensity) {
        summonStar(attacker.level(), location, attacker, intensity);
    }

    @Override
    public void onHurtEntity(LivingEntity attacker, Entity victim, float damage, int intensity) {
        summonStar(attacker.level(), victim.getPosition(0).add(0, attacker.getBbHeight() * 0.5, 0), attacker, intensity);
    }
}
