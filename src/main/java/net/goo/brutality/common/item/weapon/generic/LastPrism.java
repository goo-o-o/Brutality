package net.goo.brutality.common.item.weapon.generic;

import net.goo.brutality.common.config.BrutalityCommonConfig;
import net.goo.brutality.common.entity.projectile.ray.LastPrismRay;
import net.goo.brutality.common.item.base.BrutalityGenericItem;
import net.goo.brutality.common.registry.BrutalityDamageTypes;
import net.goo.brutality.common.registry.BrutalityEntities;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.goo.brutality.event.forge.DelayedTaskScheduler;
import net.goo.brutality.util.magic.ManaHelper;
import net.goo.brutality.util.math.phys.hitboxes.HitboxUtils;
import net.goo.brutality.util.math.phys.hitboxes.OrientedBoundingBox;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;

public class LastPrism extends BrutalityGenericItem {
    private static final String LAST_PRISM_ID = "LastPrismId";

    public LastPrism(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    public static final OrientedBoundingBox HITBOX = new OrientedBoundingBox(Vec3.ZERO, new Vec3(1.1F, 1.1F, 150).scale(0.5F), 0, 0, 0);
    public static final Vec3 OFFSET = new Vec3(0, 0, 3 + HITBOX.halfExtents.z);

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 3000;
    }


    @Override
    public int getUseDuration(@NotNull ItemStack pStack) {
        return 72000;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.BOW;
    }

    public static void setLastPrismRay(ItemStack stack, int id) {
        stack.getOrCreateTag().putInt(LAST_PRISM_ID, id);
    }

    public static Entity getLastPrismRay(Level level, ItemStack stack) {
        return level.getEntity(stack.getOrCreateTag().getInt(LAST_PRISM_ID));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (ManaHelper.getMana(pPlayer) < 15) return InteractionResultHolder.fail(stack);
        pPlayer.getCooldowns().addCooldown(stack.getItem(), 20);

        LastPrismRay lastPrismRay = new LastPrismRay(BrutalityEntities.LAST_PRISM_RAY.get(), pLevel);
        lastPrismRay.setOwner(pPlayer);
        lastPrismRay.setPos(pPlayer.getEyePosition());
        setLastPrismRay(stack, lastPrismRay.getId());
        pLevel.addFreshEntity(lastPrismRay);

        pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        if (getLastPrismRay(pLevel, pStack) instanceof LastPrismRay lastPrismRay) {
            lastPrismRay.triggerAnim("controller", "despawn");
            DelayedTaskScheduler.queueServerWork(pLevel, 20, lastPrismRay::discard);
        }

    }


    @Override
    public boolean isPerspectiveAware() {
        return true;
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity owner, ItemStack pStack, int pRemainingUseDuration) {
        if (!(owner instanceof Player player)) return;

        if (pRemainingUseDuration % 10 == 0) {
            if (ManaHelper.getMana(player) > 15) {
                ManaHelper.modifyManaValue(player, -15);
            } else {
                player.releaseUsingItem();
            }
        }

        if (getLastPrismRay(pLevel, pStack) instanceof LastPrismRay lastPrismRay) {
            if (pLevel instanceof ServerLevel serverLevel) {

                HitboxUtils.RayResult<LivingEntity> result = HitboxUtils.handleRay(player, LivingEntity.class, LastPrism.HITBOX, OFFSET, null, true);

                result.entitiesHit.forEach(
                        e -> {
                            e.hurt(BrutalityDamageTypes.last_prism(owner), BrutalityCommonConfig.LAST_PRISM_TICK_DAMAGE.get());
                            serverLevel.sendParticles(BrutalityParticles.LAST_PRISM_RAY_PARTICLE.get(), e.getX(), e.getY(0.5F), e.getZ(),
                                    1, 1, 1, 1, 0);
                        });

                lastPrismRay.setDataMaxLength(result.distance);

                Vec3 endPos = result.endPos;
                serverLevel.sendParticles(
                        BrutalityParticles.LAST_PRISM_RAY_PARTICLE.get(), endPos.x, endPos.y, endPos.z, 5, 1, 1, 1, 0);

            }
            if ((this.getUseDuration(pStack) - pRemainingUseDuration) % 6 == 0) {
//                pLevel.playSound(owner, owner.getOnPos(), BrutalityModSounds.LAST_PRISM_USE.get(), SoundSource.PLAYERS, 2F, 1F);
                owner.playSound(BrutalitySounds.LAST_PRISM_USE.get(), 3F, 1F);
            }

        }
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
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


