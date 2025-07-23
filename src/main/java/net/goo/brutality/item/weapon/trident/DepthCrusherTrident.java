package net.goo.brutality.item.weapon.trident;

import net.goo.brutality.entity.projectile.trident.physics_projectile.ThrownDepthCrusher;
import net.goo.brutality.item.base.BrutalityTridentItem;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;

public class DepthCrusherTrident extends BrutalityTridentItem {
    protected final RandomSource random = RandomSource.create();


    public DepthCrusherTrident(float attackDamageModifier, float attackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(attackDamageModifier, attackSpeedModifier, rarity, descriptionComponents);
    }


    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public float getLaunchVel() {
        return 1;
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        super.releaseUsing(pStack, pLevel, pEntityLiving, pTimeLeft);
        int i = this.getUseDuration(pStack) - pTimeLeft;
        if (pEntityLiving instanceof Player player && i >= 10) {
            player.getCooldowns().addCooldown(pStack.getItem(), 50);
        }
    }

    @Override
    public void launchProjectile(Level pLevel, Player player, ItemStack pStack) {

        ThrownDepthCrusher thrownEntity = new ThrownDepthCrusher(pLevel, player, pStack, BrutalityModEntities.THROWN_DEPTH_CRUSHER.get());
        thrownEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, getLaunchVel(), 1.0F);
        thrownEntity.setOwner(player);

        pLevel.addFreshEntity(thrownEntity);
    }

    @Override
    protected boolean isInnatelyInfinite() {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            pPlayer.startUsingItem(pHand);
            return InteractionResultHolder.consume(itemstack);
        }
    }
}
