package net.goo.brutality.item.weapon.trident;

import net.goo.brutality.entity.projectile.trident.ThrownGungnir;
import net.goo.brutality.item.base.BrutalityTridentItem;
import net.goo.brutality.registry.BrutalityModEntities;
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
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

import static net.goo.brutality.util.helpers.EnchantmentHelper.hasInfinity;
import static net.goo.brutality.util.helpers.EnchantmentHelper.restrictEnchants;

public class GungnirTrident extends BrutalityTridentItem {
    protected final RandomSource random = RandomSource.create();



    private static final Set<Enchantment> ALLOWED_ENCHANTMENTS = Set.of(
            Enchantments.MOB_LOOTING,
            Enchantments.MENDING,
            Enchantments.UNBREAKING,
            Enchantments.LOYALTY
    );

    public GungnirTrident(float attackDamageModifier, float attackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(attackDamageModifier, attackSpeedModifier, rarity, descriptionComponents);
    }


    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        stack.enchant(Enchantments.LOYALTY, 4);
        return stack;
    }

// CHANGE THESE
    //==================================================================//

    @Override
    public float getLaunchVel() {
        return 1.75F;
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

        ThrownGungnir thrownEntity = new ThrownGungnir(pLevel, player, pStack, BrutalityModEntities.THROWN_GUNGNIR_ENTITY.get());
        thrownEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, getLaunchVel(), 1.0F);
        if (player.getAbilities().instabuild) {
            thrownEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }

        pLevel.addFreshEntity(thrownEntity);
        pLevel.playSound(null, thrownEntity, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
        if (!player.getAbilities().instabuild && !hasInfinity(pStack)) {
            player.getInventory().removeItem(pStack);
        }

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

//    @Override
//    public <T extends Item & BrutalityGeoItem> void configureLayers(BrutalityItemRenderer<T> renderer) {
//        super.configureLayers(renderer);
//        renderer.addRenderLayer(new AutoGlowingGeoLayer<>(renderer));
//    }
}
