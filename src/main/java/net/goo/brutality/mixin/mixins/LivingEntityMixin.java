package net.goo.brutality.mixin.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.goo.brutality.event.LivingDodgeEvent;
import net.goo.brutality.item.BrutalityArmorMaterials;
import net.goo.brutality.item.base.BrutalityAnkletItem;
import net.goo.brutality.item.weapon.scythe.DarkinScythe;
import net.goo.brutality.registry.*;
import net.goo.brutality.util.BrutalityEntityRotations;
import net.goo.brutality.util.ModUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModLoader;
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
    @Unique
    private static final float TIMEKEEPERS_CLOCK_DEBUFF_DURATION = 0.75F, TCOFT_DEBUFF_DURATION = 0.65F, TCOFT_BUFF_DURATION = 1.25F;

    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
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

        if (CuriosApi.getCuriosInventory(entity).isPresent()) {
            ICuriosItemHandler handler = CuriosApi.getCuriosInventory(entity).orElse(null);
            if (handler.isEquipped(BrutalityModItems.THE_CLOCK_OF_FROZEN_TIME.get())) {
                if (effect.getCategory() == MobEffectCategory.HARMFUL) {
                    int duration = (int) (originalDuration * TCOFT_DEBUFF_DURATION);
                    duration = Math.max(duration, 1);

                    modifiedInstance = new MobEffectInstance(effect, duration,
                            effectInstance.getAmplifier(), effectInstance.isAmbient(), effectInstance.isVisible(), effectInstance.showIcon());
                } else if (effect.getCategory() == MobEffectCategory.BENEFICIAL) {
                    int duration = (int) (originalDuration * TCOFT_BUFF_DURATION);
                    duration = Math.max(duration, 1);

                    modifiedInstance = new MobEffectInstance(effect, duration,
                            effectInstance.getAmplifier(), effectInstance.isAmbient(), effectInstance.isVisible(), effectInstance.showIcon());
                }
            } else if (handler.isEquipped(BrutalityModItems.TIMEKEEPERS_CLOCK.get()) && effect.getCategory() == MobEffectCategory.HARMFUL) {
                int duration = (int) (originalDuration * TIMEKEEPERS_CLOCK_DEBUFF_DURATION);
                duration = Math.max(duration, 1);

                modifiedInstance = new MobEffectInstance(effect, duration,
                        effectInstance.getAmplifier(), effectInstance.isAmbient(), effectInstance.isVisible(), effectInstance.showIcon());
            }
        }

        if (effect == BrutalityModMobEffects.STUNNED.get()) {
            if (entity.getAttribute(BrutalityModAttributes.TENACITY.get()) != null) {
                int duration = (int) (originalDuration * (2 - entity.getAttributeValue(BrutalityModAttributes.TENACITY.get())));
                duration = Math.max(duration, 1);

                modifiedInstance = new MobEffectInstance(effect, duration,
                        effectInstance.getAmplifier(), effectInstance.isAmbient(), effectInstance.isVisible(), effectInstance.showIcon());

            }
        }


        return modifiedInstance;
    }


    @Inject(method = "getDamageAfterArmorAbsorb", at = @At("HEAD"), cancellable = true)
    private void alterArmorAbsorb(DamageSource pDamageSource, float pDamageAmount, CallbackInfoReturnable<Float> cir) {
        if (!pDamageSource.is(DamageTypeTags.BYPASSES_ARMOR)) {
            LivingEntity target = (LivingEntity) (Object) this;
            Entity attacker = pDamageSource.getEntity();

            if (attacker instanceof Player player) {
                // Check if attacker has the custom attributes before accessing them
                AttributeInstance lethalityAttr = player.getAttribute(BrutalityModAttributes.LETHALITY.get());
                AttributeInstance armorPenAttr = player.getAttribute(BrutalityModAttributes.ARMOR_PENETRATION.get());

                if (lethalityAttr != null && armorPenAttr != null) {
                    double lethalityValue = lethalityAttr.getValue();
                    double armorPenValue = armorPenAttr.getValue();

                    float modifiedArmor = target.getArmorValue();
                    modifiedArmor *= (float) (2 - armorPenValue);
                    modifiedArmor -= (float) lethalityValue;
                    float modifiedDamage = CombatRules.getDamageAfterAbsorb(pDamageAmount, modifiedArmor, (float) target.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
                    cir.setReturnValue(modifiedDamage);
                }
            }
        }
    }


    @ModifyReturnValue(method = "getJumpPower()F", at = @At("RETURN"))
    private float modifyJumpStrength(float original) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        AttributeInstance jumpAttr = livingEntity.getAttribute(BrutalityModAttributes.JUMP_HEIGHT.get());
        if (jumpAttr != null) {
            return (float) (original + ((jumpAttr.getValue() - 1.2522) * 0.159)); // 0.159 jump power = 1 block
        }
        return original;
    }


    @Inject(
            method = "hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void handleDodge(DamageSource pSource, float pAmount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (pSource.is(DamageTypeTags.BYPASSES_COOLDOWN)) return;
        if (livingEntity.hasEffect(BrutalityModMobEffects.DODGE_COOLDOWN.get())) return;
        AttributeInstance dodgeAttr = livingEntity.getAttribute(BrutalityModAttributes.DODGE_CHANCE.get());
        if (dodgeAttr != null && Mth.nextFloat(livingEntity.getRandom(), 0, 1) < dodgeAttr.getValue() - 1) {
            LivingDodgeEvent.Server dodgeEvent = new LivingDodgeEvent.Server(livingEntity, pSource, pAmount);
            ModLoader.get().postEvent(dodgeEvent);
            if (!dodgeEvent.isCanceled()) {
                if (livingEntity.level() instanceof ServerLevel serverLevel)
                    serverLevel.playSound(null, livingEntity.getOnPos(), ModUtils.getRandomSound(BrutalityModSounds.DODGE_SOUNDS), SoundSource.PLAYERS,
                            1F, Mth.nextFloat(livingEntity.getRandom(), 0.5F, 1.5F));

                livingEntity.addEffect(new MobEffectInstance(BrutalityModMobEffects.DODGE_COOLDOWN.get(), 10));
                CuriosApi.getCuriosInventory(livingEntity).ifPresent(itemHandler ->
                        itemHandler.getStacksHandler("anklet").ifPresent(stacksHandler -> {
                            for (int i = 0; i < stacksHandler.getSlots(); i++) {
                                ItemStack stack = stacksHandler.getStacks().getStackInSlot(i);
                                if (stack.getItem() instanceof BrutalityAnkletItem ankletItem) {
                                    ankletItem.onDodgeServer(livingEntity, pSource, pAmount, stack);
//                                            PacketHandler.sendToAllClients(new ClientboundDodgePacket(livingEntity.getId(), pSource, pAmount, stack));
                                }
                            }
                        }));
                cir.setReturnValue(false); // Cancel hurt if event not canceled
            }
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

        Attribute attributeToUse;
        double vanillaSlowdownRate;
        LivingEntity livingEntity = (((LivingEntity) (Object) this));
        if (livingEntity.onGround()) {
            attributeToUse = BrutalityModAttributes.GROUND_FRICTION.get();
            // When onGround, f3 = f2 * 0.91.
            // Vanilla Slowdown Rate = 1.0 - f3.
            vanillaSlowdownRate = 1.0D - f3;
        } else {
            attributeToUse = BrutalityModAttributes.AIR_FRICTION.get();
            // When airborne, f3 = 0.91 (Vanilla Default).
            vanillaSlowdownRate = 0.09; // 0.09D
        }

        AttributeInstance frictionAttribute = livingEntity.getAttribute(attributeToUse);
        if (frictionAttribute == null) {
            return f3; // Return the original (vanilla or environmental mod) value
        }

        double A = frictionAttribute.getValue(); // Attribute value (0 = no friction, 1 = normal, 2 = 2x)

        // --- Bedrock-Style Slowdown Scaling ---

        // 1. Calculate the NEW slowdown rate: R_new = R_vanilla * A
        double R_new = vanillaSlowdownRate * A;

        // 2. Convert back to the friction multiplier: f3_new = 1.0 - R_new
        // Constant velocity is achieved when f3_new = 1.0
        // Maximum slowdown is achieved when f3_new = 0.0
        double newF3 = 1.0D - R_new;

        // 3. Cap the value: [0.0 (full stop), 1.0 (no slowdown)]
        return (float) Mth.clamp(newF3, 0.0F, 1.0F);
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
                entity.getCapability(BrutalityCapabilities.PLAYER_BLOOD_CAP).ifPresent(cap -> cap.incrementBlood(excess * 0.5F));
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
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;add(DDD)Lnet/minecraft/world/phys/Vec3;"
            )
    )
    private Vec3 applyDirectionalSprintBoostMinimal(Vec3 instance, double x, double y, double z) {
        LivingEntity livingEntity = (((LivingEntity) (Object) this));
        Optional<ICuriosItemHandler> handlerOpt = CuriosApi.getCuriosInventory(livingEntity).resolve();
        if (handlerOpt.isPresent()) {
            ICuriosItemHandler handler = handlerOpt.get();
            if (handler.isEquipped(BrutalityModItems.OMNIDIRECTIONAL_MOVEMENT_GEAR.get())) {

                float currentX = livingEntity.xxa;
                float currentZ = livingEntity.zza;
                final double BOOST_AMOUNT = 0.2D;

                // Calculate the magnitude of the horizontal impulse vector
                double magnitude = Math.sqrt(currentX * currentX + currentZ * currentZ);

                // Check if the player is moving horizontally
                if (magnitude > 1.0E-6D) {

                    // 1. Normalize the impulse vector to get the direction (local space)
                    // (Note: This is technically redundant if we use atan2, but ensures the components are clean)
                    double normalizedX = currentX / magnitude;
                    double normalizedZ = currentZ / magnitude;

                    // Get player's look direction (yaw) in radians
                    float yaw = this.getYRot() * ((float) Math.PI / 180F);
                    double cosYaw = Mth.cos(yaw);
                    double sinYaw = Mth.sin(yaw);

                    // Standard 2D rotation matrix applied to the normalized impulse vector:
                    double rotatedX = normalizedX * cosYaw - normalizedZ * sinYaw;
                    double rotatedZ = normalizedZ * cosYaw + normalizedX * sinYaw;

                    // 3. Scale the rotated vector by the fixed sprint jump boost amount
                    double boostX = rotatedX * BOOST_AMOUNT;
                    double boostZ = rotatedZ * BOOST_AMOUNT;

                    // 4. Return the new DeltaMovement, adding the calculated directional boost
                    return instance.add(boostX, 0.0D, boostZ);
                }
            }
        }
        return instance.add(x, y, z);
    }
}

