package net.goo.brutality.item.weapon.generic;

import net.goo.brutality.entity.projectile.ray.LastPrismRay;
import net.goo.brutality.item.base.BrutalityGenericItem;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.registry.BrutalityModSounds;
import net.goo.brutality.registry.BrutalityModParticles;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;

public class LastPrismItem extends BrutalityGenericItem {
    private static String LAST_PRISM_ID = "LastPrismId";

    public LastPrismItem(Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }


    @Override
    public int getMaxDamage(ItemStack stack) {
        return 3000;
    }


    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    public static void setLastPrismRay(ItemStack stack, int id) {
        stack.getOrCreateTag().putInt(LAST_PRISM_ID, id);
    }

    public static Entity getLastPrismRay(Level level, ItemStack stack) {
        return level.getEntity(stack.getOrCreateTag().getInt(LAST_PRISM_ID));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        pPlayer.getCooldowns().addCooldown(stack.getItem(), 20);

        LastPrismRay lastPrismRay = new LastPrismRay(BrutalityModEntities.LAST_PRISM_RAY.get(), pLevel);
        lastPrismRay.setOwner(pPlayer);
        lastPrismRay.setPos(pPlayer.getLookAngle().add(pPlayer.getX(), pPlayer.getEyeY(), pPlayer.getZ()));
        setLastPrismRay(stack, lastPrismRay.getId());
        pLevel.addFreshEntity(lastPrismRay);

//        if (pLevel instanceof ServerLevel serverLevel)
//            triggerAnim(pPlayer, GeoItem.getOrAssignId(stack, serverLevel), "controller", "use");

        pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
        if (getLastPrismRay(pLevel, pStack) instanceof LastPrismRay lastPrismRay) {
            lastPrismRay.getPersistentData().putLong("DespawnTime", pLevel.getGameTime() + 20);
//            if (pLevel instanceof ServerLevel serverLevel)
//                stopTriggeredAnim(pLivingEntity, GeoItem.getOrAssignId(pStack, serverLevel), "controller", "use");
        }

    }

    @Override
    public boolean isPerspectiveAware() {
        return true;
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity owner, ItemStack pStack, int pRemainingUseDuration) {
        super.onUseTick(pLevel, owner, pStack, pRemainingUseDuration);
        if (getLastPrismRay(pLevel, pStack) instanceof LastPrismRay lastPrismRay) {
            if (pLevel instanceof ServerLevel serverLevel) {

                ModUtils.RayData<LivingEntity> rayData = ModUtils.getEntitiesInRay(LivingEntity.class, owner, 150,
                        ClipContext.Fluid.NONE, ClipContext.Block.OUTLINE, 1, e -> e != owner, 100,

                        loc -> serverLevel.sendParticles(
                                BrutalityModParticles.LAST_PRISM_RAY_PARTICLE.get(), loc.x, loc.y, loc.z, 1, 1, 1, 1, 0)

                );

                lastPrismRay.setDataMaxLength(rayData.distance() - 2);

                if (rayData.entityList() == null) return;

                for (LivingEntity target : rayData.entityList()) {
                    target.invulnerableTime = 0;
                    target.hurt(target.damageSources().indirectMagic(owner, target), 8);
                }

                Vec3 endPos = rayData.endPos();
                serverLevel.sendParticles(
                        BrutalityModParticles.LAST_PRISM_RAY_PARTICLE.get(), endPos.x, endPos.y, endPos.z, 5, 1, 1, 1, 0);


                pStack.hurtAndBreak(1, owner, e -> e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            }
            if ((this.getUseDuration(pStack) - pRemainingUseDuration) % 6 == 0) {
//                pLevel.playSound(owner, owner.getOnPos(), BrutalityModSounds.LAST_PRISM_USE.get(), SoundSource.PLAYERS, 2F, 1F);
                owner.playSound(BrutalityModSounds.LAST_PRISM_USE.get(), 3F, 1F);
            }

        }
    }


    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot slot, Entity entity) {
        if (entity instanceof LivingEntity living) {
            if (slot == EquipmentSlot.OFFHAND &&
                    living.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == this) {
                return false;
            }
        }
        return super.canEquip(stack, slot, entity);
    }


//    private <E extends GeoAnimatable> PlayState animate(AnimationState<E> state) {
//        state.getController().transitionLength(0);
//        ItemDisplayContext perspective = state.getData(DataTickets.ITEM_RENDER_PERSPECTIVE);
//        boolean thirdPerson = perspective == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || perspective == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
//
//        if (thirdPerson)
//            state.getController().triggerableAnim("use", RawAnimation.begin().thenPlay("use_tp"));
//
//        return PlayState.CONTINUE;
//    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
//        controllers.add(new AnimationController<>(this, "controller", 0, this::animate)
//        );
    }


    @Override
    public void inventoryTick(ItemStack stack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pIsSelected) return;

        if (getLastPrismRay(pLevel, stack) instanceof LastPrismRay lastPrismRay) {
            lastPrismRay.discard();
//            if (pLevel instanceof ServerLevel serverLevel && pEntity instanceof Player player) {
//
//                if (this.getAnimatableInstanceCache().getManagerForId(GeoItem.getOrAssignId(stack, serverLevel)).getAnimationControllers().get("controller").getCurrentAnimation() != null)
//                    stopTriggeredAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "use");
//            }
        }
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        if (player.level() instanceof ServerLevel serverLevel && getLastPrismRay(serverLevel, item) instanceof LastPrismRay lastPrismRay) {
            lastPrismRay.discard();

//            stopTriggeredAnim(player, GeoItem.getOrAssignId(item, serverLevel), "controller", "use");
        }
        return true;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }
}


