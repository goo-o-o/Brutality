package net.goo.brutality.mixin.mixins;

import net.goo.brutality.mixin.accessors.MobEffectInstanceSourceAccessor;
import net.goo.brutality.mob_effect.gastronomy.IGastronomyEffect;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.util.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Optional;
import java.util.Random;

@Mixin(MobEffectInstance.class)
public class MobEffectInstanceMixin implements MobEffectInstanceSourceAccessor {
    @Unique
    private final float BROKEN_CLOCK_TICK_CHANCE = 0.25F, SHATTERED_CLOCK_TICK_CHANCE = 0.25F, SUNDERED_CLOCK_TICK_CHANCE = 0.35F, TIMEKEEPERS_CLOCK_TICK_CHANCE = 0.5F, TCOFT_TICK_CHANCE = 0.6F;

    @Unique
    private Integer brutality$SourceID;  // your custom field

    @Inject(method = "<init>(Lnet/minecraft/world/effect/MobEffect;IIZZZLnet/minecraft/world/effect/MobEffectInstance;Ljava/util/Optional;)V",
            at = @At("RETURN"))
    private void onInit(MobEffect pEffect, int pDuration, int pAmplifier, boolean pAmbient, boolean pVisible, boolean pShowIcon, MobEffectInstance pHiddenEffect, Optional pFactorData, CallbackInfo ci) {
        this.brutality$SourceID = null;
    }

    @Inject(method = "save", at = @At("RETURN"))
    private void saveSource(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        if (brutality$SourceID != null) tag.putInt("SourceID", brutality$SourceID);
    }

    @Inject(method = "loadSpecifiedEffect", at = @At("RETURN"), locals = LocalCapture.PRINT)
    private static void loadSource(MobEffect pEffect, CompoundTag pNbt, CallbackInfoReturnable<MobEffectInstance> cir, int i, int j, boolean flag, boolean flag1, boolean flag2, MobEffectInstance mobeffectinstance, Optional optional) {
        if (pNbt.hasUUID("SourceID")) {
            ((MobEffectInstanceMixin) (Object) mobeffectinstance).brutality$SourceID = pNbt.getInt("SourceID");
        }
    }


    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;tickDownDuration()I"), cancellable = true)
    private void pauseEffectTickDown(LivingEntity pEntity, Runnable pOnExpirationRunnable, CallbackInfoReturnable<Boolean> cir) {
        MobEffectInstance effectInstance = (MobEffectInstance) (Object) this;

        if (pEntity.hasEffect(BrutalityModMobEffects.STEAMED.get())
                && effectInstance.getEffect() != BrutalityModMobEffects.STEAMED.get()) {
            if (effectInstance.getEffect() instanceof IGastronomyEffect)
                cir.setReturnValue(effectInstance.getDuration() > 0);
        }

        CuriosApi.getCuriosInventory(pEntity).ifPresent(handler -> {
            Random random = ModUtils.getSyncedSeededRandom(pEntity);
            if (!effectInstance.isInfiniteDuration()) {
                if (handler.findFirstCurio(BrutalityModItems.THE_CLOCK_OF_FROZEN_TIME.get()).isPresent() && random.nextFloat() < TCOFT_TICK_CHANCE) {
                    if (effectInstance.getEffect().isBeneficial()) {
                        cir.setReturnValue(effectInstance.getDuration() > 0);
                    }
                } else if (handler.findFirstCurio(BrutalityModItems.TIMEKEEPERS_CLOCK.get()).isPresent() && random.nextFloat() < TIMEKEEPERS_CLOCK_TICK_CHANCE) {
                    if (effectInstance.getEffect().isBeneficial()) {
                        cir.setReturnValue(effectInstance.getDuration() > 0);
                    }
                } else if (handler.findFirstCurio(BrutalityModItems.SUNDERED_CLOCK.get()).isPresent() && random.nextFloat() < SUNDERED_CLOCK_TICK_CHANCE) {
                    if (effectInstance.getEffect().isBeneficial()) {
                        cir.setReturnValue(effectInstance.getDuration() > 0);
                    }
                } else if (handler.findFirstCurio(BrutalityModItems.SHATTERED_CLOCK.get()).isPresent() && random.nextFloat() < SHATTERED_CLOCK_TICK_CHANCE) {
                    if (effectInstance.getEffect().isBeneficial()) {
                        cir.setReturnValue(effectInstance.getDuration() > 0);
                    }
                } else if (handler.findFirstCurio(BrutalityModItems.BROKEN_CLOCK.get()).isPresent() && random.nextFloat() < BROKEN_CLOCK_TICK_CHANCE) {
                    cir.setReturnValue(effectInstance.getDuration() > 0);
                }
            }
        });

    }

    @Override
    public Integer brutality$getSourceID() {
        return brutality$SourceID;
    }

    @Override
    public void brutality$setSourceID(int id) {
        this.brutality$SourceID = id;
    }
}
