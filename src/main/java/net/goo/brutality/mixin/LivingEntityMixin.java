package net.goo.brutality.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.goo.brutality.item.BrutalityArmorMaterials;
import net.goo.brutality.item.weapon.scythe.DarkinScythe;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.util.BrutalityEntityRotations;
import net.goo.brutality.util.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements BrutalityEntityRotations {

    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "getVisibilityPercent", at = @At("TAIL"), cancellable = true)
    private void modifyVisibilityPercent(Entity pLookingEntity, CallbackInfoReturnable<Double> cir) {
        LivingEntity self = (LivingEntity) (Object) this;


        if (ModUtils.hasFullArmorSet(self, BrutalityArmorMaterials.NOIR)) {
            cir.setReturnValue(0D);
        }
    }

    @Inject(method = "isPushable", at = @At("HEAD"), cancellable = true)
    private void managePushable(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (((LivingEntity) (Object) this));

        ItemStack mainHand = livingEntity.getMainHandItem();
        ItemStack offhand = livingEntity.getOffhandItem();


        if (mainHand.getItem() instanceof DarkinScythe)
            if (ModUtils.getTextureIdx(mainHand) == 1)
                cir.setReturnValue(false);

        if (offhand.getItem() instanceof DarkinScythe)
            if (ModUtils.getTextureIdx(offhand) == 1)
                cir.setReturnValue(false);
    }

    @WrapOperation(
            method = "travel",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getFriction(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)F")
    )
    private float wrapGetFriction(BlockState state, LevelReader level, BlockPos pos, Entity entity, Operation<Float> original) {
        float friction = original.call(state, level, pos, entity);
        if (entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(BrutalityModMobEffects.OILED.get())) {
            return (float) Math.min(1.099, 1F + livingEntity.getEffect(BrutalityModMobEffects.OILED.get()).getAmplifier() * 0.02F);
        }
        return friction;
    }

//    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getFriction(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)F"))
//    private float getFriction(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
//        if (entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(BrutalityModMobEffects.OILED.get())) {
//            return (float) Math.min(1.099, 1F + livingEntity.getEffect(BrutalityModMobEffects.OILED.get()).getAmplifier() * 0.2F);
//        }
//
//        return state.getFriction(level, pos, entity);
//    }


    @Inject(
            method = "getJumpPower",
            at = @At("RETURN"),
            cancellable = true
    )
    private void modifyJumpPower(CallbackInfoReturnable<Float> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity.hasEffect(BrutalityModMobEffects.SLICKED.get())) {
            int level = entity.getEffect(BrutalityModMobEffects.SLICKED.get()).getAmplifier() + 1;
            cir.setReturnValue(cir.getReturnValueF() * (1 - 0.15f * level));
        }

        if (entity.hasEffect(BrutalityModMobEffects.OILED.get())) {
            int level = entity.getEffect(BrutalityModMobEffects.OILED.get()).getAmplifier() + 1;
            cir.setReturnValue(cir.getReturnValueF() * (1 - 0.05f * level));
        }
    }


    @Unique
    public float brutality$prevRoll;
    @Unique
    public float brutality$roll;
    @Unique
    public float brutality$prevYaw;
    @Unique
    public float brutality$yaw;
    @Unique
    public float brutality$prevPitch;
    @Unique
    public float brutality$pitch;


    @Override
    public float brutality$getBrutalityRoll() {
        return brutality$roll;
    }

    @Override
    public float brutality$getBrutalityPrevRoll() {
        return brutality$prevRoll;
    }

    @Override
    public float brutality$getBrutalityYaw() {
        return brutality$yaw;
    }

    @Override
    public float brutality$getBrutalityPrevYaw() {
        return brutality$prevYaw;
    }

    @Override
    public float brutality$getBrutalityPitch() {
        return brutality$pitch;
    }

    @Override
    public float brutality$getBrutalityPrevPitch() {
        return brutality$prevPitch;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void calculateAngles(CallbackInfo ci) {
        this.brutality$prevRoll = this.brutality$roll;
        this.brutality$prevYaw = this.brutality$yaw;
        this.brutality$prevPitch = this.brutality$pitch;

        // Calculate current rotation values
        Vec3 motion = this.getDeltaMovement();
        float speed = (float) motion.length();
        if (speed > 0.1) {
            this.brutality$roll += speed * 30f; // Adjust multiplier for rotation speed
            this.brutality$yaw = (float) Math.atan2(motion.z, motion.x) * Mth.RAD_TO_DEG - 90.0f;
            this.brutality$pitch = (float) Math.atan2(motion.y, Math.sqrt(motion.x * motion.x + motion.z * motion.z)) * Mth.RAD_TO_DEG;
        }
    }


}

