//package net.goo.brutality.item.weapon.hammer;
//
//import net.goo.brutality.item.base.BrutalityGeoItem;
//import net.goo.brutality.item.base.BrutalityHammerItem;
//import net.goo.brutality.registry.BrutalityModMobEffects;
//import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
//import net.minecraft.world.effect.MobEffectInstance;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Rarity;
//import net.minecraft.world.item.Tier;
//import software.bernie.geckolib.core.animation.AnimatableManager;
//
//import java.util.List;
//
//public class WhiskHammer extends BrutalityHammerItem implements BrutalityGeoItem {
//
//
//    public WhiskHammer(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
//        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
//    }
//
//    @Override
//    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
//    }
//
//    @Override
//    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
//        pTarget.addEffect(new MobEffectInstance(BrutalityModMobEffects.MASHED.get(), 200, 0, false, true));
//
//        return super.hurtEnemy(pStack, pTarget, pAttacker);
//    }
//}
