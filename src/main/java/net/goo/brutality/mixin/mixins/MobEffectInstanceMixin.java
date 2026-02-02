package net.goo.brutality.mixin.mixins;

import net.goo.brutality.common.item.curios.charm.BaseBrokenClock;
import net.goo.brutality.common.mob_effect.gastronomy.wet.SteamedEffect;
import net.goo.brutality.mixin.accessors.MobEffectInstanceSourceAccessor;
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

import javax.annotation.Nullable;
import java.util.Optional;
/**
 * Injected into {@link MobEffectInstance} to track the source of an effect.
 * <p>
 * By adding a {@code SourceID} (the entity's {@code id}), we can bridge the gap
 * between an effect ticking on a victim and the original attacker who applied it.
 * </p>
 */
@Mixin(MobEffectInstance.class)
public class MobEffectInstanceMixin implements MobEffectInstanceSourceAccessor {


    /**
     * Stores the {@code entityId} of the source that applied this effect.
     * Use {@code entity.getId()} to populate this.
     */
    @Unique
    private Integer brutality$SourceID;

    /**
     * Initializes the SourceID as null whenever a new instance is created.
     */
    @Inject(method = "<init>(Lnet/minecraft/world/effect/MobEffect;IIZZZLnet/minecraft/world/effect/MobEffectInstance;Ljava/util/Optional;)V",
            at = @At("RETURN"))
    private void brutality$onInit(MobEffect pEffect, int pDuration, int pAmplifier, boolean pAmbient,
                                  boolean pVisible, boolean pShowIcon, MobEffectInstance pHiddenEffect,
                                  Optional<MobEffectInstance.FactorData> pFactorData, CallbackInfo ci) {
        this.brutality$SourceID = null;
    }

    /**
     * Persists the SourceID to the world save file.
     */
    @Inject(method = "save", at = @At("RETURN"))
    private void brutality$saveSource(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        if (this.brutality$SourceID != null) {
            tag.putInt("BrutalitySourceID", this.brutality$SourceID);
        }
    }

    /**
     * Restores the SourceID when the effect is loaded from a NBT tag (e.g., world load or chunk reload).
     */
    @Inject(method = "loadSpecifiedEffect", at = @At("RETURN"))
    private static void brutality$loadSource(MobEffect pEffect, CompoundTag pNbt,
                                             CallbackInfoReturnable<MobEffectInstance> cir) {
        MobEffectInstance instance = cir.getReturnValue();
        if (instance != null && pNbt.contains("BrutalitySourceID")) {
            ((MobEffectInstanceMixin) (Object) instance).brutality$SourceID = pNbt.getInt("BrutalitySourceID");
        }
    }

    // --- Accessors ---

    @Unique
    public void brutality$setSourceID(int id) {
        this.brutality$SourceID = id;
    }

    @Unique
    @Nullable
    public Integer brutality$getSourceID() {
        return this.brutality$SourceID;
    }


    /**
     * Intercepts the per-tick update of a MobEffectInstance to conditionally bypass duration reduction.
     * <p>
     * This injection targets the invocation of {@code tickDownDuration()}, allowing custom logic
     * to cancel the decrement. If cancelled, the effect remains active for another tick without
     * losing time.
     * </p>
     *
     * @param pEntity                The entity currently affected by this status effect.
     * @param pOnExpirationRunnable  The logic to execute if the effect expires (unused here).
     * @param cir                    The callback used to cancel the tick and return {@code true},
     * preserving the effect's current state.
     */
    @Inject(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;tickDownDuration()I"),
            cancellable = true
    )
    private void pauseEffectTickDown(LivingEntity pEntity, Runnable pOnExpirationRunnable, CallbackInfoReturnable<Boolean> cir) {
        // Cast 'this' to MobEffectInstance to access its properties (Duration, Effect Type, etc.)
        MobEffectInstance effectInstance = (MobEffectInstance) (Object) this;

        // 1. Check for environmental pauses (e.g., being in a 'Steamed' state)
        SteamedEffect.pauseTickdown(pEntity, effectInstance, cir);

        // 2. Check for item-based pauses (e.g., carrying a Broken Clock curio)
        // Note: If SteamedEffect already set the return value, this call may still run,
        // effectively giving the player multiple chances to pause the tick.
        BaseBrokenClock.pauseTickDown(pEntity, effectInstance, cir);
    }

}
