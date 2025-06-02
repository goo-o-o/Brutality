package net.goo.armament.item.noir;

import net.goo.armament.item.base.ArmaGenericItem;
import net.goo.armament.item.ModItemCategories;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class CanopyOfShadows extends ArmaGenericItem {
    public CanopyOfShadows(Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(pProperties, identifier, category, rarity, abilityCount);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pEntity instanceof LivingEntity player) {
            if (player.getMainHandItem() == pStack || player.getOffhandItem() == pStack) {
                player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 2, 2, true, false), player);

            }
        }
    }
}
