package net.goo.brutality.common.item.generic.augments.seals;

import net.goo.brutality.common.item.BrutalityCategories;
import net.goo.brutality.common.item.generic.augments.BrutalitySealAugmentItem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BombSeal extends BrutalitySealAugmentItem {
    public BombSeal(Properties pProperties, BrutalityCategories... itemTypes) {
        super(pProperties, itemTypes);
    }

    @Override
    public void onHurt(LivingEntity victim, DamageSource source, float damage, int intensity) {
        victim.level().explode(victim, victim.getX(), victim.getY(0.5), victim.getZ(), intensity * 0.5F, Level.ExplosionInteraction.NONE);
    }

    @Override
    public void onHurtByEntity(Entity attacker, LivingEntity victim, DamageSource source, float damage, int intensity) {
        victim.level().explode(victim, attacker.getX(), attacker.getY(0.5), attacker.getZ(), intensity * 0.5F, Level.ExplosionInteraction.NONE);
    }

    @Override
    public void onProcAtLocation(LivingEntity attacker, Vec3 location, int intensity) {
        attacker.level().explode(attacker, location.x, location.y, location.z, intensity * 0.5F, Level.ExplosionInteraction.NONE);
    }

    @Override
    public void onHurtEntity(LivingEntity attacker, Entity victim, float damage, int intensity) {
        attacker.level().explode(attacker, victim.getX(), victim.getY(0.5), victim.getZ(),  intensity * 0.5F, Level.ExplosionInteraction.NONE);
    }
}
