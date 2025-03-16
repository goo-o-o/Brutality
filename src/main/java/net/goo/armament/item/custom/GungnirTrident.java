package net.goo.armament.item.custom;

import net.goo.armament.client.item.ArmaGeoItem;
import net.goo.armament.client.renderers.item.AutoGlowingRenderer;
import net.goo.armament.entity.custom.ThrownGungnir;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.item.base.ArmaTridentItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.function.Consumer;

import static net.goo.armament.util.ModUtils.hasInfinity;

public class GungnirTrident extends ArmaTridentItem implements Vanishable {
    protected final RandomSource random = RandomSource.create();

    public GungnirTrident(Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(new Properties().durability(1561), identifier, category, rarity, abilityCount);
        this.colors = THUNDERBOLT_COLORS;
    }


    public ModItemCategories getCategory() {
        return category;
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
            ThrownGungnir thrownEntity = new ThrownGungnir(pLevel, player, pStack);
            thrownEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, getLaunchVel() + (float) j * 0.5F, 1.0F);
            if (player.getAbilities().instabuild) {
                thrownEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }

            pLevel.addFreshEntity(thrownEntity);
            pLevel.playSound(null, thrownEntity, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
            if (!player.getAbilities().instabuild && !hasInfinity(pStack)) {
                player.getInventory().removeItem(pStack);
            }
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
            } else if (EnchantmentHelper.getRiptide(itemstack) > 0 && !pPlayer.isInWaterOrRain()) {
                return InteractionResultHolder.fail(itemstack);
            } else {
                pPlayer.startUsingItem(pHand);
                return InteractionResultHolder.consume(itemstack);
            }
        }
    }



    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public <T extends Item & ArmaGeoItem, R extends BlockEntityWithoutLevelRenderer> void initGeo(Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
        super.initGeo(consumer, AutoGlowingRenderer.class);
    }
}
