package net.goo.brutality.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.util.SealUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentItem.class)
public class TridentItemMixin {

    @Inject(
            method = "releaseUsing",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z",
                    shift = At.Shift.BEFORE
            )
    )
    private void attachSealToTrident(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft, CallbackInfo ci, @Local ThrownTrident throwntrident) {
        SealUtils.SEAL_TYPE sealType = SealUtils.getSealType(pStack);
        if (sealType != null) {
            throwntrident.getCapability(BrutalityCapabilities.SEAL_TYPE_CAP).ifPresent(cap -> cap.setSealType(sealType));
        }
    }
}