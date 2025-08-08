package net.goo.brutality.mixin;

import net.minecraft.world.damagesource.CombatTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CombatTracker.class)
public abstract class CombatTrackerMixin {
    @Inject(
            method = "recheckStatus",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;clear()V",
                    remap = false
            )
    )
    private void preventClearIfSpecialDeath(CallbackInfo ci) {
//        CombatTracker tracker = (CombatTracker) (Object) this;
//        System.out.println(tracker.entries);
//        if (!tracker.entries.isEmpty()) {
//            CombatEntry lastEntry = tracker.entries.get(tracker.entries.size() - 1);
//            if (lastEntry.source().is(BrutalityDamageTypes.DEMATERIALIZE)) {
//                ci.cancel();
//            }
//        }
    }
}