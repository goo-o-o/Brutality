package net.goo.brutality.item.weapon.trident;

import net.goo.brutality.entity.projectile.trident.ThrownThunderbolt;
import net.goo.brutality.item.base.BrutalityTridentItem;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;
import java.util.Set;

import static java.lang.Math.PI;

public class ThunderboltTrident extends BrutalityTridentItem {
    protected final RandomSource random = RandomSource.create();

    private static final Set<Enchantment> ALLOWED_ENCHANTMENTS = Set.of(
            Enchantments.MOB_LOOTING,
            Enchantments.MENDING,
            Enchantments.UNBREAKING,
            Enchantments.LOYALTY,
            Enchantments.INFINITY_ARROWS
    );

    public ThunderboltTrident(float attackDamageModifier, float attackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(attackDamageModifier, attackSpeedModifier, rarity, descriptionComponents);
    }


    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        stack.enchant(Enchantments.INFINITY_ARROWS, 1);
        stack.enchant(Enchantments.LOYALTY, 5);
        return stack;
    }

    // CHANGE THESE
    //==================================================================//

    @Override
    public float getLaunchVel() {
        return 3.5F;
    }

    @Override
    public void launchProjectile(Level pLevel, Player player, ItemStack pStack) {
        int j = EnchantmentHelper.getRiptide(pStack);

        if (j == 0) {
            ThrownThunderbolt thrownThunderbolt = new ThrownThunderbolt(pLevel, player, pStack, BrutalityModEntities.THROWN_THUNDERBOLT_ENTITY.get());
            thrownThunderbolt.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, getLaunchVel() + (float) j * 0.5F, 1.0F);
            if (player.getAbilities().instabuild) {
                thrownThunderbolt.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }

            pLevel.addFreshEntity(thrownThunderbolt);
            pLevel.playSound(null, thrownThunderbolt, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
            if (!player.getAbilities().instabuild && !net.goo.brutality.util.helpers.EnchantmentHelper.hasInfinity(pStack)) {
                player.getInventory().removeItem(pStack);
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (pPlayer.isShiftKeyDown()) {
            performZeusWrathAttack(pPlayer, pLevel, itemstack, pHand);
            pPlayer.getCooldowns().addCooldown(itemstack.getItem(), 80);
            return InteractionResultHolder.fail(itemstack);
        } else {
            if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
                return InteractionResultHolder.fail(itemstack);
            } else if (EnchantmentHelper.getRiptide(itemstack) > 0 && !pPlayer.isInWaterOrRain()) {
                return InteractionResultHolder.fail(itemstack);
            } else {
                pPlayer.startUsingItem(pHand);
                return InteractionResultHolder.consume(itemstack);
            }
        }
    }

    public void performZeusWrathAttack(Player pPlayer, Level pLevel, ItemStack pStack, InteractionHand pHand) {
        for (int i = 0; i < random.nextInt(5, 11); i++) {
            float distance = Mth.nextFloat(random, 5F, 10F);
            float angle = random.nextFloat() * 2 * ((float) PI);

            float xOffset = distance * ((float) Math.cos(angle));
            float zOffset = distance * ((float) Math.sin(angle));

            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(pLevel);
            assert lightningBolt != null;
            lightningBolt.setPos(pPlayer.getX() + xOffset, pPlayer.getY(), pPlayer.getZ() + zOffset);
            lightningBolt.setCause(pPlayer instanceof ServerPlayer ? (ServerPlayer) pPlayer : null);
            pLevel.addFreshEntity(lightningBolt);
            pStack.hurtAndBreak(1, pPlayer, player -> pPlayer.broadcastBreakEvent(pHand));

        }
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return ALLOWED_ENCHANTMENTS.contains(enchantment);
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return net.goo.brutality.util.helpers.EnchantmentHelper.restrictEnchants(book, ALLOWED_ENCHANTMENTS);
    }
}
