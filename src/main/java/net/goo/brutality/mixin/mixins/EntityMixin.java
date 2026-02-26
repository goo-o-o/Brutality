package net.goo.brutality.mixin.mixins;

import net.goo.brutality.common.item.curios.feet.VoidSteppers;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.common.registry.BrutalityItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    protected Vec3 stuckSpeedMultiplier;

    @ModifyVariable(method = "move", ordinal = 1, index = 3, name = "vec32", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/Entity;collide(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 giveFluidCollision(Vec3 original) {

        Entity entity = (((Entity) (Object) this));

        if (original.y > 0) return original; // early return if they are moving upwards

        Level level = entity.level();

        // to ensure we don't phase through different liquid levels, iterate through nearby fluids

        // and yes, this was roughly adapted from Relics' Aqua Walker
        int[][] offsets = {
                {1, 0, 1}, {1, 0, 0}, {1, -1, 0}, {1, 0, -1},
                {0, 0, 1}, {0, 0, 0}, {0, -1, 0}, {0, 0, -1},
                {-1, 0, 1}, {-1, 0, 0}, {-1, -1, 0}, {-1, 0, -1}
        };

        double highestValue = original.y;
        FluidState highestFluid = null;

        BlockPos source = entity.blockPosition();
        for (int[] offset : offsets) {
            BlockPos targetPos = source.offset(offset[0], offset[1], offset[2]);
            FluidState fluidState = level.getFluidState(targetPos);

            if (fluidState.isEmpty()) continue;

            // get a 1x1 hitbox with the uppermost part matching the fluid
            VoxelShape shape = Shapes.block().move(targetPos.getX(), targetPos.getY() + fluidState.getOwnHeight(), targetPos.getZ());
            // inflate for robustness
            if (Shapes.joinIsNotEmpty(shape, Shapes.create(entity.getBoundingBox().inflate(0.175)), BooleanOp.AND)) {
                double height = shape.max(Direction.Axis.Y) - entity.getY() - 1;
                // absolute y coords - entity y coords - 1 to offset feet position

                if (highestValue < height) {
                    highestValue = height;
                    highestFluid = fluidState;
                }
            }

        }

        if (highestFluid == null) return original;

        // now to actually stop the player from falling
        boolean cancel = false;
        if (entity instanceof LivingEntity livingEntity) {
            Optional<ICuriosItemHandler> curiosOpt = CuriosApi.getCuriosInventory(livingEntity).resolve();
            if (curiosOpt.isPresent()) {
                ICuriosItemHandler handler = curiosOpt.get();
                if (handler.isEquipped(BrutalityItems.LAVA_WALKERS.get())) {
                    if (highestFluid.is(FluidTags.LAVA)) {
                        cancel = true;
                    }
                }
                if (handler.isEquipped(BrutalityItems.WATER_WALKERS.get())) {
                    if (highestFluid.is(FluidTags.WATER)) {
                        cancel = true;
                    }
                }
            }
        }

        if (cancel) {
            entity.fallDistance = 0;
            entity.setOnGround(true);
            return new Vec3(original.x, highestValue, original.z);
        }

        return original;
    }

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
