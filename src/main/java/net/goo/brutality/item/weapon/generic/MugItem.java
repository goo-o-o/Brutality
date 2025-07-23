package net.goo.brutality.item.weapon.generic;

import net.goo.brutality.entity.projectile.trident.physics_projectile.ThrownStyrofoamCup;
import net.goo.brutality.item.base.BrutalityGenericItem;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;

public class MugItem extends BrutalityGenericItem {
    public MugItem(Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }


    @Override
    public @Nullable FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
        FoodProperties.Builder builder = new FoodProperties.Builder()
                .nutrition(1).alwaysEat();

        if (ModUtils.getTextureIdx(stack) == 1) {
            builder.effect(() -> new MobEffectInstance(BrutalityModMobEffects.CAFFEINATED.get(), 600, 0), 1.0F);
        } else if (ModUtils.getTextureIdx(stack) == 2) {
            builder.effect(() -> new MobEffectInstance(BrutalityModMobEffects.CAFFEINATED.get(), 600, 1), 1.0F);
        }

        return builder.build();
    }

    protected String[] types = new String[]{"", "_coffee", "_eeffoc"};

    @Override
    public String texture(ItemStack stack) {
        return "mug" + types[ModUtils.getTextureIdx(stack)];
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public @NotNull Component getName(ItemStack pStack) {
        return Component.translatable("item.brutality.mug" + types[ModUtils.getTextureIdx(pStack)]);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack pStack) {
        return ModUtils.getTextureIdx(pStack) == 0 ? UseAnim.SPEAR : UseAnim.DRINK;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        if (pLivingEntity instanceof Player player) {
            int i = this.getUseDuration(pStack) - pTimeCharged;
            if (ModUtils.getTextureIdx(pStack) == 0) { // Throwing
                if (i >= 10) {
                    if (!pLevel.isClientSide) {
                        launchProjectile(pLevel, player, pStack);
                        player.getInventory().removeItem(pStack);
                    }
                }
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return ModUtils.getTextureIdx(pStack) == 0 ? 72000 : 40;
    }

    @Override
    public boolean isEdible() {
        return true;
    }

    public @NotNull ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        if (ModUtils.getTextureIdx(pStack) != 0) {
            super.finishUsingItem(pStack, pLevel, pEntityLiving);
            if (pEntityLiving instanceof ServerPlayer serverplayer) {
                CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, pStack);
                serverplayer.awardStat(Stats.ITEM_USED.get(this));
            }

            if (pStack.isEmpty()) {
                return new ItemStack(BrutalityModItems.MUG.get());
            } else {
                if (pEntityLiving instanceof Player player && !player.getAbilities().instabuild) {
                    ItemStack itemstack = new ItemStack(BrutalityModItems.MUG.get());
                    if (!player.getInventory().add(itemstack)) {
                        player.drop(itemstack, false);
                    }
                }

            }
        }
        return pStack;
    }

    public void launchProjectile(Level pLevel, Player player, ItemStack pStack) {
        ThrownStyrofoamCup thrownEntity = new ThrownStyrofoamCup(pLevel, player, pStack, BrutalityModEntities.THROWN_STYROFOAM_CUP.get());
        thrownEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0.75F, 1.0F);
        if (player.getAbilities().instabuild) {
            thrownEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }
        pLevel.addFreshEntity(thrownEntity);
    }
}
