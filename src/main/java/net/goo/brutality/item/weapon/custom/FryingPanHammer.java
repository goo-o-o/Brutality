package net.goo.brutality.item.weapon.custom;

import net.goo.brutality.item.base.BrutalityGeoItem;
import net.goo.brutality.item.base.BrutalityHammerItem;
import net.goo.brutality.registry.ModSounds;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;

public class FryingPanHammer extends BrutalityHammerItem implements BrutalityGeoItem {


    public FryingPanHammer(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, rarity, descriptionComponents);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pAttacker.level().playSound(null, pAttacker.getOnPos(), ModSounds.FRYING_PAN_HIT.get(), SoundSource.PLAYERS, 1F, 1F);
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
