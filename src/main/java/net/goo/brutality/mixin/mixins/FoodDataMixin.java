package net.goo.brutality.mixin.mixins;

import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.ModUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public abstract class FoodDataMixin {
    @Unique
    private Player brutality$currentTickPlayer;

    @Inject(method = "tick", at = @At("HEAD"))
    private void capturePlayer(Player pPlayer, CallbackInfo ci) {
        this.brutality$currentTickPlayer = pPlayer;
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void cleanupPlayer(Player pPlayer, CallbackInfo ci) {
        this.brutality$currentTickPlayer = null;
    }

    @Inject(method = "addExhaustion", at = @At("HEAD"), cancellable = true)
    private void modifyExhaustion(float pExhaustion, CallbackInfo ci) {
        if (this.brutality$currentTickPlayer != null && ModUtils.isWearingCurio(this.brutality$currentTickPlayer, BrutalityItems.NANOMACHINES.get())) {
            FoodData foodData = (FoodData) (Object) this;
            foodData.setExhaustion(Math.min(foodData.getExhaustionLevel() + pExhaustion * 2, 40.0F));
            ci.cancel();
        }
    }
}