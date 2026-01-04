package net.goo.brutality.mixin.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.goo.brutality.event.LivingDodgeEvent;
import net.goo.brutality.item.BrutalityArmorMaterials;
import net.goo.brutality.item.base.BrutalityAnkletItem;
import net.goo.brutality.item.weapon.scythe.DarkinScythe;
import net.goo.brutality.registry.*;
import net.goo.brutality.util.BrutalityEntityRotations;
import net.goo.brutality.util.ModUtils;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

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
            if (entity.getAttribute(ModAttributes.TENACITY.get()) != null) {
                int duration = (int) (originalDuration * (2 - entity.getAttributeValue(ModAttributes.TENACITY.get())));
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
                AttributeInstance lethalityAttr = player.getAttribute(ModAttributes.LETHALITY.get());
                AttributeInstance armorPenAttr = player.getAttribute(ModAttributes.ARMOR_PENETRATION.get());

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
        AttributeInstance jumpAttr = livingEntity.getAttribute(ModAttributes.JUMP_HEIGHT.get());
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
        AttributeInstance dodgeAttr = livingEntity.getAttribute(ModAttributes.DODGE_CHANCE.get());
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


    @WrapOperation(
            method = "travel",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getFriction(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)F")
    )
    private float wrapGetFriction(BlockState instance, LevelReader levelReader, BlockPos blockPos, Entity entity, Operation<Float> original) {
        if (entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(BrutalityModMobEffects.OILED.get())) {
            return (float) Math.min(1.099, 1F + livingEntity.getEffect(BrutalityModMobEffects.OILED.get()).getAmplifier() * 0.02F);
        }
        return original.call(instance, levelReader, blockPos, entity);
    }

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


}

