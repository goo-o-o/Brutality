package net.goo.brutality.mixin.mixins;

import net.goo.brutality.util.item.StatTrakUtils;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingHook.class)
public class FishingHookMixin {

    @Inject(
            method = "retrieve",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;awardStat(Lnet/minecraft/resources/ResourceLocation;I)V")
    )
    private void onAwardStat(ItemStack pStack, CallbackInfoReturnable<Integer> cir) {
        StatTrakUtils.incrementStatTrakIfPossible(pStack);
    }
}
