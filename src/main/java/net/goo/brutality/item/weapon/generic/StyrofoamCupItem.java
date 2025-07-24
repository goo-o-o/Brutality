//package net.goo.brutality.item.weapon.generic;
//
//import net.goo.brutality.entity.projectile.trident.physics_projectile.ThrownStyrofoamCup;
//import net.goo.brutality.item.base.BrutalityGenericItem;
//import net.goo.brutality.registry.BrutalityModEntities;
//import net.goo.brutality.util.ModUtils;
//import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
//import net.minecraft.advancements.CriteriaTriggers;
//import net.minecraft.network.chat.Component;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.stats.Stats;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.InteractionResultHolder;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.entity.projectile.AbstractArrow;
//import net.minecraft.world.food.FoodProperties;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.ItemUtils;
//import net.minecraft.world.item.Rarity;
//import net.minecraft.world.item.UseAnim;
//import net.minecraft.world.level.Level;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//import software.bernie.geckolib.core.animation.AnimatableManager;
//
//import java.util.List;
//
//public class StyrofoamCupItem extends BrutalityGenericItem {
//    public StyrofoamCupItem(Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
//        super(rarity, descriptionComponents);
//    }
//
//    protected String[] types = new String[]{"", "_water"};
//
//    @Override
//    public String texture(ItemStack stack) {
//        return "styrofoam_cup" + types[ModUtils.getTextureIdx(stack)];
//    }
//    @Override
//    public @NotNull Component getName(ItemStack pStack) {
//        return Component.translatable("item.brutality.styrofoam_cup" + types[ModUtils.getTextureIdx(pStack)]);
//    }
//    @Override
//    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
//        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand);
//    }
//
//    @Override
//    public @NotNull UseAnim getUseAnimation(ItemStack pStack) {
//        return ModUtils.getTextureIdx(pStack) == 0 ? UseAnim.SPEAR : UseAnim.DRINK;
//    }
//
//    @Override
//    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
//
//    }
//
//    @Override
//    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
//        if (pLivingEntity instanceof Player player) {
//            int i = this.getUseDuration(pStack) - pTimeCharged;
//            if (ModUtils.getTextureIdx(pStack) == 0) { // Throwing
//                if (i >= 10) {
//                    if (!pLevel.isClientSide) {
//                        launchProjectile(pLevel, player, pStack);
//                        player.getInventory().removeItem(pStack);
//                    }
//                }
//            }
//        }
//    }
//
//    @Override
//    public int getUseDuration(ItemStack pStack) {
//        return ModUtils.getTextureIdx(pStack) == 0 ? 72000 : 40;
//    }
//
//    @Override
//    public boolean isEdible() {
//        return true;
//    }
//
//    @Override
//    public @Nullable FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
//        return new FoodProperties.Builder().build();
//    }
//
//    @Override
//    public @NotNull ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
//        if (ModUtils.getTextureIdx(pStack) != 0) {
//            if (pLivingEntity instanceof ServerPlayer serverplayer) {
//                CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, pStack);
//                serverplayer.awardStat(Stats.ITEM_USED.get(this));
//            }
//
//
//            if (pStack.isEmpty()) {
//                return this.getDefaultInstance();
//            } else {
//                if (pLivingEntity instanceof Player player && !player.getAbilities().instabuild) {
//                    ItemStack itemstack = this.getDefaultInstance();
//                    pStack.shrink(1);
//
//                    int slot = player.getInventory().findSlotMatchingItem(pStack);
//
//                    if (slot != -1) {
//                        player.getInventory().setItem(slot, itemstack);
//                    } else {
//                        player.drop(itemstack, false);
//                    }
//                }
//
//                return pStack;
//            }
//        }
//        return ItemStack.EMPTY;
//    }
//
//    public void launchProjectile(Level pLevel, Player player, ItemStack pStack) {
//        ThrownStyrofoamCup thrownEntity = new ThrownStyrofoamCup(pLevel, player, pStack, BrutalityModEntities.THROWN_STYROFOAM_CUP.get());
//        thrownEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0.75F, 1.0F);
//        if (player.getAbilities().instabuild) {
//            thrownEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
//        }
//        pLevel.addFreshEntity(thrownEntity);
//    }
//}
