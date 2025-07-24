//package net.goo.brutality.mob_effect.gastronomy;
//
//import net.goo.brutality.registry.BrutalityModMobEffects;
//import net.minecraft.world.effect.MobEffect;
//import net.minecraft.world.effect.MobEffectCategory;
//import net.minecraft.world.entity.LivingEntity;
//
//public class HotAndSpicyEffect extends MobEffect implements IGastronomyEffect {
//
//    public HotAndSpicyEffect(MobEffectCategory pCategory, int pColor) {
//        super(pCategory, pColor);
//    }
//
//    @Override
//    public Type getType() {
//        return Type.WET;
//    }
//
//    @Override
//    public boolean scalesWithLevel() {
//        return false;
//    }
//
//    @Override
//    public boolean modifiesDamage() {
//        return false;
//    }
//
//    @Override
//    public float baseMultiplier() {
//        return 0.05F;
//    }
//
//    @Override
//    public void applyEffect(LivingEntity attacker, LivingEntity victim, int level) {
//        int amplifier = attacker.getEffect(BrutalityModMobEffects.HOT_AND_SPICY.get()).getAmplifier() + 1;
//        victim.setSecondsOnFire(amplifier * 2);
//    }
//}
