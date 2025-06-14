package net.goo.brutality.item.weapon.custom;

import net.goo.brutality.entity.custom.trident.physics_projectile.ThrownCabbage;
import net.goo.brutality.item.base.BrutalityTridentItem;
import net.goo.brutality.registry.ModEntities;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;
import java.util.Set;

import static net.goo.brutality.util.helpers.EnchantmentHelper.restrictEnchants;

public class CabbageTrident extends BrutalityTridentItem {
    protected final RandomSource random = RandomSource.create();



    private static final Set<Enchantment> ALLOWED_ENCHANTMENTS = Set.of(
    );

    public CabbageTrident(Properties pProperties, float attackDamageModifier, float attackSpeedModifier, String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pProperties, attackDamageModifier, attackSpeedModifier, identifier, rarity, descriptionComponents);
    }


    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 16;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public float getLaunchVel() {
        return 1F;
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
    public void launchProjectile(Level pLevel, Player player, ItemStack pStack) {

        ThrownCabbage thrownEntity = new ThrownCabbage(pLevel, player, pStack, ModEntities.THROWN_CABBAGE_ENTITY.get());
        thrownEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, getLaunchVel(), 1.0F);
        if (player.getAbilities().instabuild) {
            thrownEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }

        pLevel.addFreshEntity(thrownEntity);
        pLevel.playSound(null, thrownEntity, pLevel.getRandom().nextBoolean() ? SoundEvents.MOSS_FALL : SoundEvents.MOSS_HIT, SoundSource.BLOCKS, 1.0F, 1.0F);
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
