package net.goo.armament.item.weapon.custom;

import net.goo.armament.client.renderers.item.ArmaAutoGlowingItemRenderer;
import net.goo.armament.entity.custom.trident.ThrownGungnir;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.item.weapon.base.ArmaTridentItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.Set;
import java.util.function.Consumer;

import static net.goo.armament.util.helpers.EnchantmentHelper.hasInfinity;
import static net.goo.armament.util.helpers.EnchantmentHelper.restrictEnchants;

public class GungnirTrident extends ArmaTridentItem implements Vanishable {
    protected final RandomSource random = RandomSource.create();

    public GungnirTrident(Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(new Properties().durability(1561), identifier, category, rarity, abilityCount);
    }

    private static final Set<Enchantment> ALLOWED_ENCHANTMENTS = Set.of(
            Enchantments.MOB_LOOTING,
            Enchantments.MENDING,
            Enchantments.UNBREAKING,
            Enchantments.LOYALTY
    );



    // CHANGE THESE
    //==================================================================//

    @Override
    public float getLaunchVel() {
        return 2F;
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

        ThrownGungnir thrownEntity = new ThrownGungnir(pLevel, player, pStack);
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

    //==================================================================//

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (pPlayer.isCrouching()) {
            pPlayer.getCooldowns().addCooldown(itemstack.getItem(), 80);
            return InteractionResultHolder.fail(itemstack);
        } else {
            if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
                return InteractionResultHolder.fail(itemstack);
            } else {
                pPlayer.startUsingItem(pHand);
                return InteractionResultHolder.consume(itemstack);
            }
        }
    }
    

    @Override
    public <R extends BlockEntityWithoutLevelRenderer> void initGeo(Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
        super.initGeo(consumer, ArmaAutoGlowingItemRenderer.class);
    }
}
