package net.goo.brutality.common.item.generic.augments.seals;

import net.goo.brutality.common.entity.spells.voidwalker.GraviticImplosionEntity;
import net.goo.brutality.common.item.BrutalityCategories;
import net.goo.brutality.common.item.generic.augments.BrutalitySealAugmentItem;
import net.goo.brutality.common.registry.BrutalityEntities;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class VoidSeal extends BrutalitySealAugmentItem {
    public VoidSeal(Properties pProperties, BrutalityCategories... itemTypes) {
        super(pProperties, itemTypes);
    }

    private void summonGraviticImplosion(Level level, Vec3 targetPos, LivingEntity owner, int intensity) {
        GraviticImplosionEntity spellEntity = new GraviticImplosionEntity(BrutalityEntities.GRAVITIC_IMPLOSION_ENTITY.get(), level);
        spellEntity.setSpellLevel(intensity);


        spellEntity.setPos(targetPos);
        spellEntity.setOwner(owner);

        level.addFreshEntity(spellEntity);
    }

    @Override
    public void onHurt(LivingEntity victim, DamageSource source, float damage, int intensity) {
        Level level = victim.level();
        summonGraviticImplosion(level, victim.getRopeHoldPosition(0), victim, intensity);
    }

    @Override
    public void onHurtByEntity(Entity attacker, LivingEntity victim, DamageSource source, float damage, int intensity) {
        summonGraviticImplosion(attacker.level(), attacker.getPosition(0).add(0, attacker.getBbHeight() * 0.5, 0), victim, intensity);
    }

    @Override
    public void onProcAtLocation(LivingEntity attacker, Vec3 location, int intensity) {
        summonGraviticImplosion(attacker.level(), location, attacker, intensity);
    }

    @Override
    public void onHurtEntity(LivingEntity attacker, Entity victim, float damage, int intensity) {
        summonGraviticImplosion(attacker.level(), victim.getPosition(0).add(0, attacker.getBbHeight() * 0.5, 0), attacker, intensity);
    }
}
