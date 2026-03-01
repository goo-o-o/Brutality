package net.goo.brutality.mixin.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.goo.brutality.common.entity.capabilities.BrutalityCapabilities;
import net.goo.brutality.common.item.BrutalityArmorMaterials;
import net.goo.brutality.common.item.curios.charm.BaseBrokenClock;
import net.goo.brutality.common.item.curios.charm.OmnidirectionalMovementGear;
import net.goo.brutality.common.item.weapon.scythe.DarkinScythe;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.common.mixin_helpers.IBrutalityAttribute;
import net.goo.brutality.util.BrutalityEntityRotations;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.attribute.AttributeCalculationHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements BrutalityEntityRotations {


    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void brutality$initAttributeOwner(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        ((IBrutalityAttribute) self.getAttributes()).brutality$setOwner(self);
    }


    @ModifyVariable(
            method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z",
            at = @At("HEAD"),
            argsOnly = true
    )
    private MobEffectInstance modifyEffectDuration(MobEffectInstance effectInstance) {
        LivingEntity entity = (LivingEntity) (Object) this;
        MobEffect effect = effectInstance.getEffect();
        int originalDuration = effectInstance.getDuration();
        MobEffectInstance modifiedInstance = effectInstance;


        modifiedInstance = BaseBrokenClock.getModifiedTimeEffect(entity, modifiedInstance);

        // todo: readd tenacity and stun


        return modifiedInstance;
    }


    @Inject(method = "getDamageAfterArmorAbsorb", at = @At("HEAD"), cancellable = true)
    private void alterArmorAbsorb(DamageSource pDamageSource, float pDamageAmount, CallbackInfoReturnable<Float> cir) {
        if (!pDamageSource.is(DamageTypeTags.BYPASSES_ARMOR)) {
            LivingEntity target = (LivingEntity) (Object) this;
            Entity attacker = pDamageSource.getEntity();

            if (attacker instanceof Player player) {
                // Check if attacker has the custom attributes before accessing them
                AttributeInstance lethalityAttr = player.getAttribute(BrutalityAttributes.LETHALITY.get());
                AttributeInstance armorPenAttr = player.getAttribute(BrutalityAttributes.ARMOR_PENETRATION.get());

                if (lethalityAttr != null && armorPenAttr != null) {
                    double lethalityValue = lethalityAttr.getValue();
                    double armorPenValue = armorPenAttr.getValue();

                    float modifiedArmor = target.getArmorValue();
                    modifiedArmor *= (float) (1 - armorPenValue); // invert so 20% armor pen = 80% effective armor
                    modifiedArmor -= (float) lethalityValue;
                    float modifiedDamage = CombatRules.getDamageAfterAbsorb(pDamageAmount, modifiedArmor, (float) target.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
                    cir.setReturnValue(modifiedDamage);
                }
            }
        }
    }


    @ModifyReturnValue(method = "getJumpPower()F", at = @At("RETURN"))
    private float modifyGetJumpPowerReturnValue(float original) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;

        Float modified = AttributeCalculationHelper.getJumpPowerFromAttribute(livingEntity, original);
        if (modified != null) {
            return modified;
        }

        return original;
    }


    @Inject(
            method = "hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onHurt(DamageSource pSource, float pAmount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        AttributeCalculationHelper.handleDodge(livingEntity, pSource, pAmount, cir);
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

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;add(DDD)Lnet/minecraft/world/phys/Vec3;", ordinal = 1))
    private Vec3 modifyElytraAcceleration(Vec3 instance, double x, double y, double z) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;

        double multiplier = 1.0;
        if (CuriosApi.getCuriosInventory(livingEntity).map(h -> h.isEquipped(BrutalityItems.WAY_OF_THE_WIND.get())).orElse(false)) {
            multiplier = 3;
        }

        return instance.add(x * multiplier, y * multiplier, z * multiplier);
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getFallDamageSound(I)Lnet/minecraft/sounds/SoundEvent;"))
    private SoundEvent modifyElytraHitWallSound(LivingEntity instance, int pHeight) {
        Optional<ICuriosItemHandler> curiosOpt = CuriosApi.getCuriosInventory(instance).resolve();
        if (curiosOpt.isPresent()) {
            if (curiosOpt.get().isEquipped(BrutalityItems.HEAD_CUSHION.get())) {
                return SoundEvents.WOOL_FALL;
            }
        }
        return pHeight > 4 ? instance.getFallSounds().big() : instance.getFallSounds().small();
    }

    @Inject(
            method = "travel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z",
                    ordinal = 0
            ),
            cancellable = true
    )
    private void brutality$modifyElytraDamage(Vec3 vec3, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        CuriosApi.getCuriosInventory(entity).ifPresent(handler -> {
            if (handler.isEquipped(BrutalityItems.HEAD_CUSHION.get())) ci.cancel();
        });
    }


//
//    @ModifyConstant(method = "travel", constant = @Constant(doubleValue = 0.04D))
//    private double buffLift(double constant) {
//        return constant * 3 * 0.5;
//    }
//    @ModifyConstant(method = "travel", constant = @Constant(doubleValue = 0.1D, ordinal = 1))
//    private double buffTurnAlignment(double constant) {
//        return constant * 3.0D;
//    }
//    @ModifyVariable(method = "travel", at = @At(value = "STORE", ordinal = 0), name = "d6")
//    private double multiplySwoop(double d6) {
//        LivingEntity livingEntity = (LivingEntity) (Object) this;
//
//        if (CuriosApi.getCuriosInventory(livingEntity).map(h -> h.isEquipped(BrutalityItems.WAY_OF_THE_WIND.get())).orElse(false)) {
//            return d6 * 3;
//        }
//        return d6;
//    }
//    @ModifyArgs(
//            method = "travel",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;multiply(DDD)Lnet/minecraft/world/phys/Vec3;")
//    )
//    private void applyElytraSpeed(Args args) {
//        // Index 0 = x, 1 = y, 2 = z
//        // We only want to apply this if we are actually fall flying
//        if (((LivingEntity) (Object) this).isFallFlying()) {
//            args.set(0, 0.998D); // 5x less horizontal drag
//            args.set(1, 0.996D); // 5x less vertical drag
//            args.set(2, 0.998D); // 5x less horizontal drag
//        }
//    }

    @ModifyVariable(
            method = "travel",
            at = @At(
                    value = "STORE",
                    ordinal = 0,
                    // Target the STORE of f3 after the block friction call
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getFriction(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)F"
            ),
            name = "f3"
    )
    private float modifyFrictionFactorF3(float f3) {
        return AttributeCalculationHelper.handleFriction((((LivingEntity) (Object) this)), f3);
    }

    @Inject(
            method = "heal",
            at = @At("HEAD"),
            cancellable = true
    )
    private void handleOverheal(float pHealAmount, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (ModUtils.hasFullArmorSet(entity, BrutalityArmorMaterials.VAMPIRE_LORD)) {
            float currentHealth = entity.getHealth();
            if (currentHealth > 0.0F) {
                float excess = (currentHealth + pHealAmount) - entity.getMaxHealth();
                entity.getCapability(BrutalityCapabilities.BLOOD).ifPresent(cap -> cap.modifyBloodValue(excess * 0.5F));
                entity.setHealth(currentHealth + pHealAmount);
            }

            if (entity instanceof Player player) {
                FoodData foodData = player.getFoodData();
                int amount = (int) (pHealAmount * 0.25F);
                if (foodData.needsFood()) {
                    foodData.setFoodLevel(amount + foodData.getFoodLevel());
                } else {
                    foodData.setSaturation(amount + foodData.getSaturationLevel());
                }
            }
            ci.cancel();
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


    @Redirect(
            method = "jumpFromGround",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;add(DDD)Lnet/minecraft/world/phys/Vec3;")
    )
    private Vec3 redirectJumpFromGround(Vec3 deltaMovement, double x, double y, double z) {
        LivingEntity entity = (LivingEntity) (Object) this;

        Vec3 omniBoost = OmnidirectionalMovementGear.getOmnidirectionalJumpBoost(entity);
        if (omniBoost != null) {
            return deltaMovement.add(omniBoost.x, 0.0D, omniBoost.z);
        }

        return deltaMovement.add(x, y, z);
    }


}

