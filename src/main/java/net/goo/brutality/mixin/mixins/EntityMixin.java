package net.goo.brutality.mixin.mixins;

import net.goo.brutality.common.item.curios.feet.VoidSteppers;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.common.registry.BrutalityItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    protected Vec3 stuckSpeedMultiplier;


    @Inject(method = "dampensVibrations", at = @At("HEAD"), cancellable = true)
    private void cancelVibrations(CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (((Entity) (Object) this));

        if (entity instanceof LivingEntity livingEntity) {
            CuriosApi.getCuriosInventory(livingEntity).ifPresent(handler -> handler.findFirstCurio(stack -> stack.getItem() instanceof VoidSteppers).ifPresent(slotResult -> cir.setReturnValue(true)));
        }
    }

    @Inject(method = "makeStuckInBlock", at = @At("TAIL"))
    private void bypassBlockSlowdown(BlockState pState, Vec3 pMotionMultiplier, CallbackInfo ci) {
        Entity entity = ((Entity) (Object) this);
        if (entity instanceof LivingEntity livingEntity) {
            CuriosApi.getCuriosInventory(livingEntity).ifPresent(handler -> {
                if (handler.isEquipped(BrutalityItems.GLOBETROTTERS_BADGE.get())) {
                    this.stuckSpeedMultiplier = Vec3.ZERO;
                    return;
                }

                if (handler.isEquipped(BrutalityItems.SCOUTS_BADGE.get())) {
                    if (pState.is(Blocks.COBWEB) || pState.is(Blocks.SWEET_BERRY_BUSH)) {
                        double x = Math.min(1.0, this.stuckSpeedMultiplier.x * 2);
                        double y = Math.min(1.0, this.stuckSpeedMultiplier.y * 2);
                        double z = Math.min(1.0, this.stuckSpeedMultiplier.z * 2);
                        this.stuckSpeedMultiplier = new Vec3(x, y, z);
                    }
                }
            });
        }

    }

    @Redirect(
            method = "baseTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z")
    )
    private boolean redirectHurt(Entity instance, DamageSource pSource, float pAmount) {
        if (instance instanceof LivingEntity livingEntity) {

            if (pSource.is(DamageTypes.ON_FIRE) || pSource.is(DamageTypes.IN_FIRE) || pSource.is(DamageTypes.LAVA)) {
                if (livingEntity.hasEffect(BrutalityEffects.OILED.get())) {
                    instance.invulnerableTime = 0;
                    int amplifier = livingEntity.getEffect(BrutalityEffects.OILED.get()).getAmplifier();
                    return instance.hurt(pSource, amplifier + 1);
                }
            }

        }

        return instance.hurt(pSource, pAmount);
    }

    @ModifyArg(method = "setSecondsOnFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setRemainingFireTicks(I)V"), index = 0)
    private int halveFireTicks(int originalTime) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof LivingEntity livingEntity) {

            return CuriosApi.getCuriosInventory(livingEntity)
                    .filter(handler -> handler.isEquipped(BrutalityItems.FIRE_EXTINGUISHER.get()))
                    .map(handler -> Math.max(1, ((int) (originalTime * 0.5))))
                    .orElse(originalTime);
        }
        return originalTime;
    }


    @Inject(method = "playStepSound", at = @At("HEAD"), cancellable = true)
    private void cancelStepSound(BlockPos pPos, BlockState pState, CallbackInfo ci) {
        VoidSteppers.cancelSoundIfNeeded((((Entity) (Object) this)), ci);
    }

    @Inject(method = "playCombinationStepSounds", at = @At("HEAD"), cancellable = true, remap = false)
    private void cancelCombinationStepSound(BlockState primaryState, BlockState secondaryState, BlockPos primaryPos, BlockPos secondaryPos, CallbackInfo ci) {
        VoidSteppers.cancelSoundIfNeeded((((Entity) (Object) this)), ci);
    }

    @Inject(method = "playMuffledStepSound", at = @At("HEAD"), cancellable = true, remap = false)
    private void cancelMuffledStepSound(BlockState state, BlockPos pos, CallbackInfo ci) {
        VoidSteppers.cancelSoundIfNeeded((((Entity) (Object) this)), ci);
    }

    @Inject(method = "walkingStepSound", at = @At("HEAD"), cancellable = true, remap = false)
    private void cancelWalkingStepSound(BlockPos pPos, BlockState pState, CallbackInfo ci) {
        VoidSteppers.cancelSoundIfNeeded((((Entity) (Object) this)), ci);
    }

    @Inject(method = "playAmethystStepSound", at = @At("HEAD"), cancellable = true, remap = false)
    private void cancelWalkingStepSound(CallbackInfo ci) {
        VoidSteppers.cancelSoundIfNeeded((((Entity) (Object) this)), ci);
    }



}
