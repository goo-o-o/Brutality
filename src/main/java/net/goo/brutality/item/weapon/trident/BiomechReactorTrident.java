package net.goo.brutality.item.weapon.trident;

import net.goo.brutality.entity.projectile.trident.physics_projectile.ThrownBiomechReactor;
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
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;
import java.util.Set;

import static net.goo.brutality.util.helpers.EnchantmentHelper.restrictEnchants;

public class BiomechReactorTrident extends BrutalityTridentItem {
    protected final RandomSource random = RandomSource.create();


    private static final Set<Enchantment> ALLOWED_ENCHANTMENTS = Set.of(
    );

    public BiomechReactorTrident(float attackDamageModifier, float attackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
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
        return 0.75F;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return restrictEnchants(book, ALLOWED_ENCHANTMENTS);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return ALLOWED_ENCHANTMENTS.contains(enchantment);
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

        ThrownBiomechReactor thrownEntity = new ThrownBiomechReactor(pLevel, player, pStack, BrutalityModEntities.THROWN_BIOMECH_REACTOR.get());
        thrownEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, getLaunchVel(), 1.0F);

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
